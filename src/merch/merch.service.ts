import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

function toMerchItem(m: any) {
  return {
    id: m.id,
    name: m.name,
    type: m.type,
    productionCost: m.productionCost,
    batchSize: m.batchSize,
    sellingPrice: m.sellingPrice,
    fixedCosts: m.fixedCosts,
    stock: m.stock ?? 0,
    hasSizes: m.hasSizes ?? false,
    stockSizes: (m.stockSizes as Record<string, number> | null) ?? undefined,
    notes: m.notes ?? undefined,
    createdAt: m.createdAt.toISOString(),
  };
}

@Injectable()
export class MerchService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const rows = await this.prisma.merchItem.findMany({
      where: { bandId },
      orderBy: { createdAt: 'asc' },
    });
    return rows.map(toMerchItem);
  }

  async create(bandId: string, dto: any) {
    const { id: _id, createdAt: _ca, ...data } = dto;
    // Compute total stock from sizes if hasSizes
    if (data.hasSizes && data.stockSizes) {
      data.stock = Object.values(data.stockSizes as Record<string, number>).reduce((a: number, b: number) => a + b, 0);
    }
    const m = await this.prisma.merchItem.create({
      data: { ...data, bandId },
    });
    return toMerchItem(m);
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
    // Recompute total stock from sizes if hasSizes
    if (data.hasSizes && data.stockSizes) {
      data.stock = Object.values(data.stockSizes as Record<string, number>).reduce((a: number, b: number) => a + b, 0);
    }
    const m = await this.prisma.merchItem.update({
      where: { id },
      data,
    });
    return toMerchItem(m);
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.merchItem.delete({ where: { id } });
  }

  /** Restock: set stock for simple items or per-size for sized items */
  async restock(bandId: string, id: string, dto: { stock?: number; stockSizes?: Record<string, number> }) {
    await this.#findOwned(bandId, id);
    const updateData: any = {};
    if (dto.stockSizes !== undefined) {
      updateData.stockSizes = dto.stockSizes;
      updateData.stock = Object.values(dto.stockSizes).reduce((a, b) => a + b, 0);
    } else if (dto.stock !== undefined) {
      updateData.stock = dto.stock;
    }
    const m = await this.prisma.merchItem.update({ where: { id }, data: updateData });
    return toMerchItem(m);
  }

  /** TPV: sell units, reduce stock (optionally per size), create income Transaction */
  async sell(bandId: string, id: string, dto: { quantity: number; unitPrice: number; date: string; size?: string; notes?: string; gigId?: string }) {
    const item = await this.#findOwned(bandId, id);
    if (dto.quantity <= 0) throw new BadRequestException('La cantidad debe ser mayor que 0');

    let stockUpdate: any;

    if (item.hasSizes && dto.size) {
      const sizes = (item.stockSizes as Record<string, number>) ?? {};
      const sizeStock = sizes[dto.size] ?? 0;
      if (sizeStock < dto.quantity) {
        throw new BadRequestException(`Stock insuficiente en talla ${dto.size} (disponible: ${sizeStock})`);
      }
      const newSizes = { ...sizes, [dto.size]: sizeStock - dto.quantity };
      const newTotal = Object.values(newSizes).reduce((a, b) => a + b, 0);
      stockUpdate = { stockSizes: newSizes, stock: newTotal };
    } else {
      if (item.stock < dto.quantity) {
        throw new BadRequestException(`Stock insuficiente (disponible: ${item.stock})`);
      }
      stockUpdate = { stock: { decrement: dto.quantity } };
    }

    const sizeLabel = dto.size ? ` [${dto.size}]` : '';
    const totalAmount = dto.quantity * dto.unitPrice;

    const [updatedItem, transaction] = await this.prisma.$transaction([
      this.prisma.merchItem.update({ where: { id }, data: stockUpdate }),
      this.prisma.transaction.create({
        data: {
          bandId,
          type: 'income',
          category: 'merch_sales',
          amount: totalAmount,
          date: dto.date,
          description: `Venta merch: ${dto.quantity}× ${item.name}${sizeLabel} @ ${dto.unitPrice}€${dto.notes ? ` — ${dto.notes}` : ''}`,
          gigId: dto.gigId ?? null,
        },
      }),
    ]);

    return {
      item: toMerchItem(updatedItem),
      transaction: {
        id: transaction.id,
        type: transaction.type,
        category: transaction.category,
        amount: transaction.amount,
        date: transaction.date,
        description: transaction.description,
        createdAt: transaction.createdAt.toISOString(),
      },
    };
  }

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.merchItem.findUnique({ where: { id } });
    if (!m || m.bandId !== bandId) throw new NotFoundException('Merch item not found');
    return m;
  }

  // ── Waiting List ──────────────────────────────────────────────────

  async getAllWaiting(bandId: string) {
    const rows = await this.prisma.merchWaitingEntry.findMany({
      where: { bandId },
      include: { item: { select: { id: true, name: true, type: true } } },
      orderBy: { createdAt: 'asc' },
    });
    return rows.map(r => this.#toEntry(r));
  }

  async addToWaitingList(bandId: string, itemId: string, dto: { name: string; quantity?: number; size?: string; contact?: string; notes?: string }) {
    await this.#findOwned(bandId, itemId);
    const r = await this.prisma.merchWaitingEntry.create({
      data: { bandId, itemId, quantity: dto.quantity ?? 1, name: dto.name, size: dto.size, contact: dto.contact, notes: dto.notes },
      include: { item: { select: { id: true, name: true, type: true } } },
    });
    return this.#toEntry(r);
  }

  async updateWaitingEntry(bandId: string, entryId: string, dto: { status?: string; contact?: string; notes?: string }) {
    const entry = await this.prisma.merchWaitingEntry.findUnique({ where: { id: entryId } });
    if (!entry || entry.bandId !== bandId) throw new NotFoundException('Entry not found');
    const r = await this.prisma.merchWaitingEntry.update({
      where: { id: entryId }, data: dto,
      include: { item: { select: { id: true, name: true, type: true } } },
    });
    return this.#toEntry(r);
  }

  #toEntry(r: any) {
    return {
      id: r.id, itemId: r.itemId, itemName: r.item.name, itemType: r.item.type,
      name: r.name, quantity: r.quantity ?? 1, size: r.size ?? undefined,
      contact: r.contact ?? undefined, notes: r.notes ?? undefined,
      status: r.status, createdAt: r.createdAt.toISOString(),
    };
  }

  async removeWaitingEntry(bandId: string, entryId: string) {
    const entry = await this.prisma.merchWaitingEntry.findUnique({ where: { id: entryId } });
    if (!entry || entry.bandId !== bandId) throw new NotFoundException('Entry not found');
    await this.prisma.merchWaitingEntry.delete({ where: { id: entryId } });
  }
}

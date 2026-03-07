import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

function toTransaction(t: any) {
  return {
    id: t.id,
    type: t.type,
    category: t.category,
    amount: t.amount,
    date: t.date,
    description: t.description ?? undefined,
    gigId: t.gigId ?? undefined,
    createdAt: t.createdAt.toISOString(),
  };
}

function toWishListItem(w: any) {
  return {
    id: w.id,
    name: w.name,
    category: w.category,
    estimatedPrice: w.estimatedPrice ?? undefined,
    priority: w.priority,
    notes: w.notes ?? undefined,
    purchased: w.purchased,
    createdAt: w.createdAt.toISOString(),
  };
}

@Injectable()
export class FinanceService {
  constructor(private readonly prisma: PrismaService) {}

  // -- Transactions --

  async findAllTransactions(bandId: string) {
    const rows = await this.prisma.transaction.findMany({
      where: { bandId },
      orderBy: { date: 'desc' },
    });
    return rows.map(toTransaction);
  }

  async createTransaction(bandId: string, dto: any) {
    const { id: _id, createdAt: _ca, ...data } = dto;
    const t = await this.prisma.transaction.create({
      data: { ...data, bandId },
    });
    return toTransaction(t);
  }

  async updateTransaction(bandId: string, id: string, dto: any) {
    await this.#findOwnedTransaction(bandId, id);
    const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
    const t = await this.prisma.transaction.update({
      where: { id },
      data,
    });
    return toTransaction(t);
  }

  async removeTransaction(bandId: string, id: string) {
    await this.#findOwnedTransaction(bandId, id);
    await this.prisma.transaction.delete({ where: { id } });
  }

  async #findOwnedTransaction(bandId: string, id: string) {
    const t = await this.prisma.transaction.findUnique({ where: { id } });
    if (!t || t.bandId !== bandId) throw new NotFoundException('Transaction not found');
    return t;
  }

  // -- Wish List --

  async findAllWishList(bandId: string) {
    const rows = await this.prisma.wishListItem.findMany({
      where: { bandId },
      orderBy: { createdAt: 'asc' },
    });
    return rows.map(toWishListItem);
  }

  async createWishListItem(bandId: string, dto: any) {
    const { id: _id, createdAt: _ca, ...data } = dto;
    const w = await this.prisma.wishListItem.create({
      data: { ...data, bandId },
    });
    return toWishListItem(w);
  }

  async updateWishListItem(bandId: string, id: string, dto: any) {
    await this.#findOwnedWishItem(bandId, id);
    const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
    const w = await this.prisma.wishListItem.update({
      where: { id },
      data,
    });
    return toWishListItem(w);
  }

  async removeWishListItem(bandId: string, id: string) {
    await this.#findOwnedWishItem(bandId, id);
    await this.prisma.wishListItem.delete({ where: { id } });
  }

  async #findOwnedWishItem(bandId: string, id: string) {
    const w = await this.prisma.wishListItem.findUnique({ where: { id } });
    if (!w || w.bandId !== bandId) throw new NotFoundException('Wish list item not found');
    return w;
  }
}

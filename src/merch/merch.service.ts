import { Injectable, NotFoundException } from '@nestjs/common';
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
    const m = await this.prisma.merchItem.create({
      data: { ...data, bandId },
    });
    return toMerchItem(m);
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
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

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.merchItem.findUnique({ where: { id } });
    if (!m || m.bandId !== bandId) throw new NotFoundException('Merch item not found');
    return m;
  }
}

import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class VenuesService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const rows = await this.prisma.venue.findMany({ where: { bandId }, orderBy: { name: 'asc' } });
    return rows.map(v => ({ ...v, createdAt: v.createdAt.toISOString() }));
  }

  async create(bandId: string, dto: any) {
    const { id: _id, createdAt: _ca, ...data } = dto;
    const v = await this.prisma.venue.create({ data: { ...data, bandId } });
    return { ...v, createdAt: v.createdAt.toISOString() };
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, createdAt: _ca, ...data } = dto;
    const v = await this.prisma.venue.update({ where: { id }, data });
    return { ...v, createdAt: v.createdAt.toISOString() };
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.venue.delete({ where: { id } });
  }

  async #findOwned(bandId: string, id: string) {
    const v = await this.prisma.venue.findFirst({ where: { id, bandId } });
    if (!v) throw new NotFoundException('Venue not found');
    return v;
  }
}

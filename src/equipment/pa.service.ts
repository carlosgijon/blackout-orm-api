import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class PaService {
  constructor(private readonly prisma: PrismaService) {}

  findAll(bandId: string) {
    return this.prisma.paEquipment.findMany({ where: { bandId } });
  }

  create(bandId: string, dto: any) {
    const { id: _id, ...data } = dto;
    return this.prisma.paEquipment.create({ data: { ...data, bandId } });
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, ...data } = dto;
    return this.prisma.paEquipment.update({ where: { id }, data });
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.paEquipment.delete({ where: { id } });
  }

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.paEquipment.findFirst({ where: { id, bandId } });
    if (!m) throw new NotFoundException('PA equipment not found');
    return m;
  }
}

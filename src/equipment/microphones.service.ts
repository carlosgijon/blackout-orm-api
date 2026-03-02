import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class MicrophonesService {
  constructor(private readonly prisma: PrismaService) {}

  findAll(bandId: string) {
    return this.prisma.microphone.findMany({ where: { bandId } });
  }

  create(bandId: string, dto: any) {
    const { id: _id, ...data } = dto;
    return this.prisma.microphone.create({ data: { ...data, bandId } });
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, ...data } = dto;
    return this.prisma.microphone.update({ where: { id }, data });
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.microphone.delete({ where: { id } });
  }

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.microphone.findFirst({ where: { id, bandId } });
    if (!m) throw new NotFoundException('Microphone not found');
    return m;
  }
}

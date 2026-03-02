import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class AmplifiersService {
  constructor(private readonly prisma: PrismaService) {}

  findAll(bandId: string) {
    return this.prisma.amplifier.findMany({ where: { bandId } });
  }

  create(bandId: string, dto: any) {
    const { id: _id, ...data } = dto;
    return this.prisma.amplifier.create({ data: { ...data, bandId } });
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, ...data } = dto;
    return this.prisma.amplifier.update({ where: { id }, data });
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.amplifier.delete({ where: { id } });
  }

  async updateInstrumentLink(bandId: string, ampId: string, instrumentId: string | null) {
    await this.#findOwned(bandId, ampId);
    // Store instrument link via instrument's ampId field
    if (instrumentId) {
      await this.prisma.instrument.updateMany({
        where: { ampId, bandId },
        data: { ampId: null },
      });
      await this.prisma.instrument.update({
        where: { id: instrumentId },
        data: { ampId },
      });
    } else {
      await this.prisma.instrument.updateMany({
        where: { ampId, bandId },
        data: { ampId: null },
      });
    }
  }

  async setMics(bandId: string, amplifierId: string, micIds: string[]) {
    await this.#findOwned(bandId, amplifierId);
    await this.prisma.microphone.updateMany({
      where: { bandId, assignedToType: 'amplifier', assignedToId: amplifierId },
      data: { assignedToType: null, assignedToId: null },
    });
    if (micIds.length > 0) {
      await this.prisma.microphone.updateMany({
        where: { bandId, id: { in: micIds } },
        data: { assignedToType: 'amplifier', assignedToId: amplifierId },
      });
    }
  }

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.amplifier.findFirst({ where: { id, bandId } });
    if (!m) throw new NotFoundException('Amplifier not found');
    return m;
  }
}

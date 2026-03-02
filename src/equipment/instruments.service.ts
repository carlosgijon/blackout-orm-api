import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class InstrumentsService {
  constructor(private readonly prisma: PrismaService) {}

  findAll(bandId: string) {
    return this.prisma.instrument.findMany({ where: { bandId }, orderBy: { channelOrder: 'asc' } });
  }

  create(bandId: string, dto: any) {
    const { id: _id, ...data } = dto;
    return this.prisma.instrument.create({ data: { ...data, bandId } });
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, ...data } = dto;
    return this.prisma.instrument.update({ where: { id }, data });
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.instrument.delete({ where: { id } });
  }

  async setMics(bandId: string, instrumentId: string, micIds: string[]) {
    await this.#findOwned(bandId, instrumentId);
    // Clear existing mic assignments for this instrument
    await this.prisma.microphone.updateMany({
      where: { bandId, assignedToType: 'instrument', assignedToId: instrumentId },
      data: { assignedToType: null, assignedToId: null },
    });
    // Set new assignments
    if (micIds.length > 0) {
      await this.prisma.microphone.updateMany({
        where: { bandId, id: { in: micIds } },
        data: { assignedToType: 'instrument', assignedToId: instrumentId },
      });
    }
  }

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.instrument.findFirst({ where: { id, bandId } });
    if (!m) throw new NotFoundException('Instrument not found');
    return m;
  }
}

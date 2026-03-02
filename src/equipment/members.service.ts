import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

function toMember(m: any) {
  return {
    id: m.id,
    name: m.name,
    roles: JSON.parse(m.roles ?? '[]'),
    stagePosition: m.stagePosition ?? undefined,
    vocalMicId: m.vocalMicId ?? undefined,
    notes: m.notes ?? undefined,
    sortOrder: m.sortOrder,
  };
}

@Injectable()
export class MembersService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const rows = await this.prisma.bandMember.findMany({ where: { bandId }, orderBy: { sortOrder: 'asc' } });
    return rows.map(toMember);
  }

  async create(bandId: string, dto: any) {
    const row = await this.prisma.bandMember.create({
      data: {
        bandId,
        name: dto.name,
        roles: JSON.stringify(dto.roles ?? []),
        stagePosition: dto.stagePosition ?? null,
        vocalMicId: dto.vocalMicId ?? null,
        notes: dto.notes ?? null,
        sortOrder: dto.sortOrder ?? 0,
      },
    });
    return toMember(row);
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const row = await this.prisma.bandMember.update({
      where: { id },
      data: {
        name: dto.name,
        roles: JSON.stringify(dto.roles ?? []),
        stagePosition: dto.stagePosition ?? null,
        vocalMicId: dto.vocalMicId ?? null,
        notes: dto.notes ?? null,
        sortOrder: dto.sortOrder ?? 0,
      },
    });
    return toMember(row);
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.bandMember.delete({ where: { id } });
  }

  async #findOwned(bandId: string, id: string) {
    const m = await this.prisma.bandMember.findFirst({ where: { id, bandId } });
    if (!m) throw new NotFoundException('Member not found');
    return m;
  }
}

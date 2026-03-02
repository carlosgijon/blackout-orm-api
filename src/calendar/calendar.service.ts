import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

function toEvent(e: any) {
  return {
    id: e.id,
    type: e.type,
    title: e.title,
    date: e.date,
    endDate: e.endDate ?? undefined,
    memberId: e.memberId ?? undefined,
    memberName: e.member?.name ?? undefined,
    allDay: e.allDay,
    notes: e.notes ?? undefined,
    createdAt: e.createdAt.toISOString(),
  };
}

const MEMBER_INCLUDE = { member: { select: { name: true } } } as const;

@Injectable()
export class CalendarService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const rows = await this.prisma.calendarEvent.findMany({
      where: { bandId },
      orderBy: { date: 'asc' },
      include: MEMBER_INCLUDE,
    });
    return rows.map(toEvent);
  }

  async getByMonth(bandId: string, year: number, month: number) {
    const from = `${year}-${String(month).padStart(2, '0')}-01`;
    const toDate = new Date(year, month, 1); // month is 1-based: next month's 1st
    const to = `${toDate.getFullYear()}-${String(toDate.getMonth() + 1).padStart(2, '0')}-01`;
    const rows = await this.prisma.calendarEvent.findMany({
      where: { bandId, date: { gte: from, lt: to } },
      orderBy: { date: 'asc' },
      include: MEMBER_INCLUDE,
    });
    return rows.map(toEvent);
  }

  async create(bandId: string, dto: any) {
    const { id: _id, createdAt: _ca, memberName: _mn, ...data } = dto;
    const row = await this.prisma.calendarEvent.create({
      data: { ...data, bandId },
      include: MEMBER_INCLUDE,
    });
    return toEvent(row);
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, createdAt: _ca, memberName: _mn, ...data } = dto;
    const row = await this.prisma.calendarEvent.update({
      where: { id },
      data,
      include: MEMBER_INCLUDE,
    });
    return toEvent(row);
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.calendarEvent.delete({ where: { id } });
  }

  async #findOwned(bandId: string, id: string) {
    const e = await this.prisma.calendarEvent.findFirst({ where: { id, bandId } });
    if (!e) throw new NotFoundException('Calendar event not found');
    return e;
  }
}

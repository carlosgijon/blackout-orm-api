import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

function toGig(g: any) {
  return {
    id: g.id,
    venueId: g.venueId ?? undefined,
    venueName: g.venue?.name ?? undefined,
    setlistId: g.setlistId ?? undefined,
    title: g.title,
    date: g.date ?? undefined,
    time: g.time ?? undefined,
    status: g.status,
    pay: g.pay ?? undefined,
    loadInTime: g.loadInTime ?? undefined,
    soundcheckTime: g.soundcheckTime ?? undefined,
    setTime: g.setTime ?? undefined,
    notes: g.notes ?? undefined,
    attendance: g.attendance ?? undefined,
    followUpDate: g.followUpDate ?? undefined,
    followUpNote: g.followUpNote ?? undefined,
    createdAt: g.createdAt.toISOString(),
  };
}

const GIG_INCLUDE = { venue: { select: { name: true } } } as const;

@Injectable()
export class GigsService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const rows = await this.prisma.gig.findMany({
      where: { bandId },
      orderBy: { createdAt: 'desc' },
      include: GIG_INCLUDE,
    });
    return rows.map(toGig);
  }

  async create(bandId: string, dto: any) {
    const { id: _id, createdAt: _ca, venueName: _vn, ...data } = dto;
    const g = await this.prisma.gig.create({ data: { ...data, bandId }, include: GIG_INCLUDE });
    return toGig(g);
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    const { id: _id, bandId: _bid, createdAt: _ca, venueName: _vn, ...data } = dto;
    const g = await this.prisma.gig.update({ where: { id }, data, include: GIG_INCLUDE });
    return toGig(g);
  }

  async updateStatus(bandId: string, id: string, status: string) {
    await this.#findOwned(bandId, id);
    const g = await this.prisma.gig.update({ where: { id }, data: { status }, include: GIG_INCLUDE });
    return toGig(g);
  }

  async updateFollowUp(bandId: string, id: string, dto: { followUpDate?: string; followUpNote?: string }) {
    await this.#findOwned(bandId, id);
    await this.prisma.gig.update({
      where: { id },
      data: { followUpDate: dto.followUpDate ?? null, followUpNote: dto.followUpNote ?? null },
    });
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.gig.delete({ where: { id } });
  }

  // -- Contacts --

  async getContacts(bandId: string, gigId: string) {
    await this.#findOwned(bandId, gigId);
    const rows = await this.prisma.gigContact.findMany({
      where: { gigId },
      orderBy: { date: 'desc' },
    });
    return rows.map(c => ({ ...c, createdAt: c.createdAt.toISOString() }));
  }

  async createContact(bandId: string, gigId: string, dto: any) {
    await this.#findOwned(bandId, gigId);
    const { id: _id, createdAt: _ca, ...data } = dto;
    const c = await this.prisma.gigContact.create({ data: { ...data, gigId } });
    return { ...c, createdAt: c.createdAt.toISOString() };
  }

  async removeContact(bandId: string, gigId: string, contactId: string) {
    await this.#findOwned(bandId, gigId);
    await this.prisma.gigContact.delete({ where: { id: contactId } });
  }

  // -- Checklists --

  async getChecklists(bandId: string, gigId: string) {
    await this.#findOwned(bandId, gigId);
    const rows = await this.prisma.gigChecklist.findMany({
      where: { gigId },
      orderBy: { createdAt: 'asc' },
    });
    return rows.map(cl => ({ ...cl, createdAt: cl.createdAt.toISOString() }));
  }

  async createChecklist(bandId: string, gigId: string, dto: { name: string }) {
    await this.#findOwned(bandId, gigId);
    const cl = await this.prisma.gigChecklist.create({ data: { gigId, name: dto.name } });
    return { ...cl, createdAt: cl.createdAt.toISOString() };
  }

  async removeChecklist(bandId: string, gigId: string, checklistId: string) {
    await this.#findOwned(bandId, gigId);
    await this.prisma.gigChecklist.delete({ where: { id: checklistId } });
  }

  // -- Checklist Items --

  async getItems(bandId: string, checklistId: string) {
    // Verify ownership via gig
    const cl = await this.prisma.gigChecklist.findFirst({
      where: { id: checklistId, gig: { bandId } },
    });
    if (!cl) throw new NotFoundException('Checklist not found');
    return this.prisma.checklistItem.findMany({
      where: { checklistId },
      orderBy: { sortOrder: 'asc' },
    });
  }

  async createItem(bandId: string, checklistId: string, dto: any) {
    const cl = await this.prisma.gigChecklist.findFirst({
      where: { id: checklistId, gig: { bandId } },
    });
    if (!cl) throw new NotFoundException('Checklist not found');
    const { id: _id, ...data } = dto;
    return this.prisma.checklistItem.create({ data: { ...data, checklistId } });
  }

  async updateItem(bandId: string, checklistId: string, itemId: string, dto: any) {
    const cl = await this.prisma.gigChecklist.findFirst({
      where: { id: checklistId, gig: { bandId } },
    });
    if (!cl) throw new NotFoundException('Checklist not found');
    const { id: _id, checklistId: _cid, ...data } = dto;
    return this.prisma.checklistItem.update({ where: { id: itemId }, data });
  }

  async removeItem(bandId: string, checklistId: string, itemId: string) {
    const cl = await this.prisma.gigChecklist.findFirst({
      where: { id: checklistId, gig: { bandId } },
    });
    if (!cl) throw new NotFoundException('Checklist not found');
    await this.prisma.checklistItem.delete({ where: { id: itemId } });
  }

  async resetItems(bandId: string, checklistId: string) {
    const cl = await this.prisma.gigChecklist.findFirst({
      where: { id: checklistId, gig: { bandId } },
    });
    if (!cl) throw new NotFoundException('Checklist not found');
    await this.prisma.checklistItem.updateMany({ where: { checklistId }, data: { done: false } });
  }

  // -- By-ID deletes (no gigId required from caller) --

  async removeContactById(bandId: string, contactId: string) {
    const contact = await this.prisma.gigContact.findFirst({
      where: { id: contactId, gig: { bandId } },
    });
    if (!contact) throw new NotFoundException('Contact not found');
    await this.prisma.gigContact.delete({ where: { id: contactId } });
  }

  async removeChecklistById(bandId: string, checklistId: string) {
    const cl = await this.prisma.gigChecklist.findFirst({
      where: { id: checklistId, gig: { bandId } },
    });
    if (!cl) throw new NotFoundException('Checklist not found');
    await this.prisma.gigChecklist.delete({ where: { id: checklistId } });
  }

  async removeItemById(bandId: string, itemId: string) {
    const item = await this.prisma.checklistItem.findFirst({
      where: { id: itemId, checklist: { gig: { bandId } } },
    });
    if (!item) throw new NotFoundException('Checklist item not found');
    await this.prisma.checklistItem.delete({ where: { id: itemId } });
  }

  async getSummary(bandId: string, gigId: string) {
    const gig = await this.#findOwned(bandId, gigId);

    const transactions = await this.prisma.transaction.findMany({
      where: { gigId, bandId },
      orderBy: { date: 'desc' },
    });

    // Merch sales are transactions with category='merch_sales' linked to this gig
    const merchSales = transactions.filter(t => t.category === 'merch_sales');

    return {
      gig: toGig({ ...gig, venue: gig.venueId ? await this.prisma.venue.findUnique({ where: { id: gig.venueId } }) : null }),
      transactions: transactions.map(t => ({
        id: t.id, type: t.type, category: t.category, amount: t.amount,
        date: t.date, description: t.description ?? undefined,
        gigId: t.gigId ?? undefined, createdAt: t.createdAt.toISOString(),
      })),
      merchSales: merchSales.map(t => ({
        id: t.id, amount: t.amount, date: t.date,
        description: t.description ?? undefined, createdAt: t.createdAt.toISOString(),
      })),
    };
  }

  async #findOwned(bandId: string, id: string) {
    const g = await this.prisma.gig.findFirst({ where: { id, bandId } });
    if (!g) throw new NotFoundException('Gig not found');
    return g;
  }
}

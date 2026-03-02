"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.GigsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
function toGig(g) {
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
        followUpDate: g.followUpDate ?? undefined,
        followUpNote: g.followUpNote ?? undefined,
        createdAt: g.createdAt.toISOString(),
    };
}
const GIG_INCLUDE = { venue: { select: { name: true } } };
let GigsService = class GigsService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const rows = await this.prisma.gig.findMany({
            where: { bandId },
            orderBy: { createdAt: 'desc' },
            include: GIG_INCLUDE,
        });
        return rows.map(toGig);
    }
    async create(bandId, dto) {
        const { id: _id, createdAt: _ca, venueName: _vn, ...data } = dto;
        const g = await this.prisma.gig.create({ data: { ...data, bandId }, include: GIG_INCLUDE });
        return toGig(g);
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const { id: _id, bandId: _bid, createdAt: _ca, venueName: _vn, ...data } = dto;
        const g = await this.prisma.gig.update({ where: { id }, data, include: GIG_INCLUDE });
        return toGig(g);
    }
    async updateStatus(bandId, id, status) {
        await this.#findOwned(bandId, id);
        const g = await this.prisma.gig.update({ where: { id }, data: { status }, include: GIG_INCLUDE });
        return toGig(g);
    }
    async updateFollowUp(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        await this.prisma.gig.update({
            where: { id },
            data: { followUpDate: dto.followUpDate ?? null, followUpNote: dto.followUpNote ?? null },
        });
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.gig.delete({ where: { id } });
    }
    async getContacts(bandId, gigId) {
        await this.#findOwned(bandId, gigId);
        const rows = await this.prisma.gigContact.findMany({
            where: { gigId },
            orderBy: { date: 'desc' },
        });
        return rows.map(c => ({ ...c, createdAt: c.createdAt.toISOString() }));
    }
    async createContact(bandId, gigId, dto) {
        await this.#findOwned(bandId, gigId);
        const { id: _id, createdAt: _ca, ...data } = dto;
        const c = await this.prisma.gigContact.create({ data: { ...data, gigId } });
        return { ...c, createdAt: c.createdAt.toISOString() };
    }
    async removeContact(bandId, gigId, contactId) {
        await this.#findOwned(bandId, gigId);
        await this.prisma.gigContact.delete({ where: { id: contactId } });
    }
    async getChecklists(bandId, gigId) {
        await this.#findOwned(bandId, gigId);
        const rows = await this.prisma.gigChecklist.findMany({
            where: { gigId },
            orderBy: { createdAt: 'asc' },
        });
        return rows.map(cl => ({ ...cl, createdAt: cl.createdAt.toISOString() }));
    }
    async createChecklist(bandId, gigId, dto) {
        await this.#findOwned(bandId, gigId);
        const cl = await this.prisma.gigChecklist.create({ data: { gigId, name: dto.name } });
        return { ...cl, createdAt: cl.createdAt.toISOString() };
    }
    async removeChecklist(bandId, gigId, checklistId) {
        await this.#findOwned(bandId, gigId);
        await this.prisma.gigChecklist.delete({ where: { id: checklistId } });
    }
    async getItems(bandId, checklistId) {
        const cl = await this.prisma.gigChecklist.findFirst({
            where: { id: checklistId, gig: { bandId } },
        });
        if (!cl)
            throw new common_1.NotFoundException('Checklist not found');
        return this.prisma.checklistItem.findMany({
            where: { checklistId },
            orderBy: { sortOrder: 'asc' },
        });
    }
    async createItem(bandId, checklistId, dto) {
        const cl = await this.prisma.gigChecklist.findFirst({
            where: { id: checklistId, gig: { bandId } },
        });
        if (!cl)
            throw new common_1.NotFoundException('Checklist not found');
        const { id: _id, ...data } = dto;
        return this.prisma.checklistItem.create({ data: { ...data, checklistId } });
    }
    async updateItem(bandId, checklistId, itemId, dto) {
        const cl = await this.prisma.gigChecklist.findFirst({
            where: { id: checklistId, gig: { bandId } },
        });
        if (!cl)
            throw new common_1.NotFoundException('Checklist not found');
        const { id: _id, checklistId: _cid, ...data } = dto;
        return this.prisma.checklistItem.update({ where: { id: itemId }, data });
    }
    async removeItem(bandId, checklistId, itemId) {
        const cl = await this.prisma.gigChecklist.findFirst({
            where: { id: checklistId, gig: { bandId } },
        });
        if (!cl)
            throw new common_1.NotFoundException('Checklist not found');
        await this.prisma.checklistItem.delete({ where: { id: itemId } });
    }
    async resetItems(bandId, checklistId) {
        const cl = await this.prisma.gigChecklist.findFirst({
            where: { id: checklistId, gig: { bandId } },
        });
        if (!cl)
            throw new common_1.NotFoundException('Checklist not found');
        await this.prisma.checklistItem.updateMany({ where: { checklistId }, data: { done: false } });
    }
    async removeContactById(bandId, contactId) {
        const contact = await this.prisma.gigContact.findFirst({
            where: { id: contactId, gig: { bandId } },
        });
        if (!contact)
            throw new common_1.NotFoundException('Contact not found');
        await this.prisma.gigContact.delete({ where: { id: contactId } });
    }
    async removeChecklistById(bandId, checklistId) {
        const cl = await this.prisma.gigChecklist.findFirst({
            where: { id: checklistId, gig: { bandId } },
        });
        if (!cl)
            throw new common_1.NotFoundException('Checklist not found');
        await this.prisma.gigChecklist.delete({ where: { id: checklistId } });
    }
    async removeItemById(bandId, itemId) {
        const item = await this.prisma.checklistItem.findFirst({
            where: { id: itemId, checklist: { gig: { bandId } } },
        });
        if (!item)
            throw new common_1.NotFoundException('Checklist item not found');
        await this.prisma.checklistItem.delete({ where: { id: itemId } });
    }
    async #findOwned(bandId, id) {
        const g = await this.prisma.gig.findFirst({ where: { id, bandId } });
        if (!g)
            throw new common_1.NotFoundException('Gig not found');
        return g;
    }
};
exports.GigsService = GigsService;
exports.GigsService = GigsService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], GigsService);
//# sourceMappingURL=gigs.service.js.map
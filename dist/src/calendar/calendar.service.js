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
exports.CalendarService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
function toEvent(e) {
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
const MEMBER_INCLUDE = { member: { select: { name: true } } };
let CalendarService = class CalendarService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const rows = await this.prisma.calendarEvent.findMany({
            where: { bandId },
            orderBy: { date: 'asc' },
            include: MEMBER_INCLUDE,
        });
        return rows.map(toEvent);
    }
    async getByMonth(bandId, year, month) {
        const from = `${year}-${String(month).padStart(2, '0')}-01`;
        const toDate = new Date(year, month, 1);
        const to = `${toDate.getFullYear()}-${String(toDate.getMonth() + 1).padStart(2, '0')}-01`;
        const rows = await this.prisma.calendarEvent.findMany({
            where: { bandId, date: { gte: from, lt: to } },
            orderBy: { date: 'asc' },
            include: MEMBER_INCLUDE,
        });
        return rows.map(toEvent);
    }
    async create(bandId, dto) {
        const { id: _id, createdAt: _ca, memberName: _mn, ...data } = dto;
        const row = await this.prisma.calendarEvent.create({
            data: { ...data, bandId },
            include: MEMBER_INCLUDE,
        });
        return toEvent(row);
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const { id: _id, bandId: _bid, createdAt: _ca, memberName: _mn, ...data } = dto;
        const row = await this.prisma.calendarEvent.update({
            where: { id },
            data,
            include: MEMBER_INCLUDE,
        });
        return toEvent(row);
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.calendarEvent.delete({ where: { id } });
    }
    async #findOwned(bandId, id) {
        const e = await this.prisma.calendarEvent.findFirst({ where: { id, bandId } });
        if (!e)
            throw new common_1.NotFoundException('Calendar event not found');
        return e;
    }
};
exports.CalendarService = CalendarService;
exports.CalendarService = CalendarService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], CalendarService);
//# sourceMappingURL=calendar.service.js.map
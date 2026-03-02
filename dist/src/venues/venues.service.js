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
exports.VenuesService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let VenuesService = class VenuesService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const rows = await this.prisma.venue.findMany({ where: { bandId }, orderBy: { name: 'asc' } });
        return rows.map(v => ({ ...v, createdAt: v.createdAt.toISOString() }));
    }
    async create(bandId, dto) {
        const { id: _id, createdAt: _ca, ...data } = dto;
        const v = await this.prisma.venue.create({ data: { ...data, bandId } });
        return { ...v, createdAt: v.createdAt.toISOString() };
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const { id: _id, bandId: _bid, createdAt: _ca, ...data } = dto;
        const v = await this.prisma.venue.update({ where: { id }, data });
        return { ...v, createdAt: v.createdAt.toISOString() };
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.venue.delete({ where: { id } });
    }
    async #findOwned(bandId, id) {
        const v = await this.prisma.venue.findFirst({ where: { id, bandId } });
        if (!v)
            throw new common_1.NotFoundException('Venue not found');
        return v;
    }
};
exports.VenuesService = VenuesService;
exports.VenuesService = VenuesService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], VenuesService);
//# sourceMappingURL=venues.service.js.map
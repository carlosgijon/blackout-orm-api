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
exports.MembersService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
function toMember(m) {
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
let MembersService = class MembersService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const rows = await this.prisma.bandMember.findMany({ where: { bandId }, orderBy: { sortOrder: 'asc' } });
        return rows.map(toMember);
    }
    async create(bandId, dto) {
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
    async update(bandId, id, dto) {
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
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.bandMember.delete({ where: { id } });
    }
    async #findOwned(bandId, id) {
        const m = await this.prisma.bandMember.findFirst({ where: { id, bandId } });
        if (!m)
            throw new common_1.NotFoundException('Member not found');
        return m;
    }
};
exports.MembersService = MembersService;
exports.MembersService = MembersService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], MembersService);
//# sourceMappingURL=members.service.js.map
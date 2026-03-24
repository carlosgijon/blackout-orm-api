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
exports.RehearsalsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let RehearsalsService = class RehearsalsService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const rehearsals = await this.prisma.rehearsal.findMany({
            where: { bandId },
            include: {
                songs: {
                    include: { song: { select: { id: true, title: true, artist: true, tempo: true, style: true } } },
                },
            },
            orderBy: { date: 'desc' },
        });
        return rehearsals.map(r => ({
            id: r.id,
            date: r.date,
            notes: r.notes ?? undefined,
            createdAt: r.createdAt.toISOString(),
            songs: r.songs.map(rs => ({
                id: rs.id,
                songId: rs.song.id,
                title: rs.song.title,
                artist: rs.song.artist,
                tempo: rs.song.tempo ?? undefined,
                style: rs.song.style ?? undefined,
                notes: rs.notes ?? undefined,
                rating: rs.rating ?? undefined,
            })),
        }));
    }
    async create(bandId, dto) {
        const r = await this.prisma.rehearsal.create({
            data: { bandId, date: dto.date, notes: dto.notes },
            include: { songs: true },
        });
        return { id: r.id, date: r.date, notes: r.notes ?? undefined, createdAt: r.createdAt.toISOString(), songs: [] };
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const r = await this.prisma.rehearsal.update({ where: { id }, data: dto });
        return { id: r.id, date: r.date, notes: r.notes ?? undefined, createdAt: r.createdAt.toISOString() };
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.rehearsal.delete({ where: { id } });
    }
    async addSong(bandId, rehearsalId, dto) {
        await this.#findOwned(bandId, rehearsalId);
        const rs = await this.prisma.rehearsalSong.create({
            data: { rehearsalId, songId: dto.songId, notes: dto.notes, rating: dto.rating },
            include: { song: { select: { id: true, title: true, artist: true, tempo: true, style: true } } },
        });
        return {
            id: rs.id,
            songId: rs.song.id,
            title: rs.song.title,
            artist: rs.song.artist,
            tempo: rs.song.tempo ?? undefined,
            style: rs.song.style ?? undefined,
            notes: rs.notes ?? undefined,
            rating: rs.rating ?? undefined,
        };
    }
    async updateSong(bandId, rehearsalId, entryId, dto) {
        await this.#findOwned(bandId, rehearsalId);
        const rs = await this.prisma.rehearsalSong.update({
            where: { id: entryId },
            data: dto,
            include: { song: { select: { id: true, title: true, artist: true, tempo: true, style: true } } },
        });
        return {
            id: rs.id,
            songId: rs.song.id,
            title: rs.song.title,
            artist: rs.song.artist,
            tempo: rs.song.tempo ?? undefined,
            style: rs.song.style ?? undefined,
            notes: rs.notes ?? undefined,
            rating: rs.rating ?? undefined,
        };
    }
    async removeSong(bandId, rehearsalId, entryId) {
        await this.#findOwned(bandId, rehearsalId);
        await this.prisma.rehearsalSong.delete({ where: { id: entryId } });
    }
    async #findOwned(bandId, id) {
        const r = await this.prisma.rehearsal.findFirst({ where: { id, bandId } });
        if (!r)
            throw new common_1.NotFoundException('Rehearsal not found');
        return r;
    }
};
exports.RehearsalsService = RehearsalsService;
exports.RehearsalsService = RehearsalsService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], RehearsalsService);
//# sourceMappingURL=rehearsals.service.js.map
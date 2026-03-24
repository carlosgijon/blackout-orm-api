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
exports.LibraryService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let LibraryService = class LibraryService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    findAll(bandId) {
        return this.prisma.librarySong.findMany({ where: { bandId }, orderBy: { title: 'asc' } });
    }
    create(bandId, dto) {
        return this.prisma.librarySong.create({ data: { ...dto, bandId } });
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        return this.prisma.librarySong.update({ where: { id }, data: dto });
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.librarySong.delete({ where: { id } });
    }
    async getStats(bandId) {
        const songs = await this.prisma.librarySong.findMany({ where: { bandId } });
        const usageCounts = await this.prisma.playlistSong.groupBy({
            by: ['songId'],
            where: { playlist: { bandId }, songId: { not: null } },
            _count: { songId: true },
        });
        const countMap = new Map(usageCounts.map(u => [u.songId, u._count.songId]));
        const withCount = songs.map(s => ({ ...s, usageCount: countMap.get(s.id) ?? 0 }));
        const byGenre = {};
        for (const s of songs) {
            const g = (s.style?.trim() || 'Sin género');
            byGenre[g] = (byGenre[g] ?? 0) + 1;
        }
        const mostUsed = [...withCount]
            .filter(s => s.usageCount > 0)
            .sort((a, b) => b.usageCount - a.usageCount)
            .slice(0, 10);
        const neverUsed = withCount.filter(s => s.usageCount === 0);
        return {
            totalSongs: songs.length,
            totalWithTempo: songs.filter(s => s.tempo).length,
            totalWithDuration: songs.filter(s => s.duration).length,
            byGenre,
            mostUsed,
            neverUsed,
        };
    }
    async getUsage(bandId, id) {
        await this.#findOwned(bandId, id);
        const entries = await this.prisma.playlistSong.findMany({
            where: { songId: id },
            include: { playlist: { select: { name: true } } },
        });
        return [...new Set(entries.map(e => e.playlist.name))];
    }
    async #findOwned(bandId, id) {
        const song = await this.prisma.librarySong.findFirst({ where: { id, bandId } });
        if (!song)
            throw new common_1.NotFoundException('Song not found');
        return song;
    }
};
exports.LibraryService = LibraryService;
exports.LibraryService = LibraryService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], LibraryService);
//# sourceMappingURL=library.service.js.map
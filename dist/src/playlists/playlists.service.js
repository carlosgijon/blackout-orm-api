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
exports.PlaylistsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
const SONG_INCLUDE = {
    song: {
        select: { title: true, artist: true, album: true, duration: true, tempo: true, style: true, notes: true },
    },
};
function toView(ps) {
    return {
        id: ps.id,
        playlistId: ps.playlistId,
        songId: ps.songId ?? undefined,
        position: ps.position,
        type: ps.type ?? 'song',
        title: ps.title ?? ps.song?.title ?? '',
        setlistName: ps.setlistName ?? undefined,
        joinWithNext: ps.joinWithNext,
        artist: ps.song?.artist ?? '',
        album: ps.song?.album ?? undefined,
        duration: ps.song?.duration ?? undefined,
        tempo: ps.song?.tempo ?? undefined,
        style: ps.song?.style ?? undefined,
        notes: ps.song?.notes ?? undefined,
    };
}
let PlaylistsService = class PlaylistsService {
    prisma;
    async getGigs(bandId, playlistId) {
        await this.#findOwned(bandId, playlistId);
        const gigs = await this.prisma.gig.findMany({
            where: { bandId, setlistId: playlistId },
            include: { venue: { select: { name: true } } },
            orderBy: { date: 'desc' },
        });
        return gigs.map(g => ({
            id: g.id,
            title: g.title,
            date: g.date ?? undefined,
            status: g.status,
            venueName: g.venue?.name ?? undefined,
        }));
    }
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const playlists = await this.prisma.playlist.findMany({
            where: { bandId },
            orderBy: { createdAt: 'desc' },
        });
        const withDurations = await Promise.all(playlists.map(async (p) => {
            const songs = await this.prisma.playlistSong.findMany({
                where: { playlistId: p.id },
                include: { song: { select: { duration: true } } },
            });
            const totalDuration = songs.reduce((acc, s) => acc + (s.song?.duration ?? 0), 0);
            const songCount = songs.filter(s => s.type === 'song').length;
            return {
                id: p.id,
                name: p.name,
                description: p.description ?? undefined,
                createdAt: p.createdAt.toISOString(),
                songCount,
                totalDuration,
            };
        }));
        return withDurations;
    }
    async create(bandId, dto) {
        const p = await this.prisma.playlist.create({ data: { bandId, name: dto.name, description: dto.description } });
        return { id: p.id, name: p.name, description: p.description ?? undefined, createdAt: p.createdAt.toISOString() };
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const p = await this.prisma.playlist.update({ where: { id }, data: { name: dto.name, description: dto.description } });
        return { id: p.id, name: p.name, description: p.description ?? undefined, createdAt: p.createdAt.toISOString() };
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.playlist.delete({ where: { id } });
    }
    async getSongs(bandId, playlistId) {
        await this.#findOwned(bandId, playlistId);
        const rows = await this.prisma.playlistSong.findMany({
            where: { playlistId },
            orderBy: { position: 'asc' },
            include: SONG_INCLUDE,
        });
        return rows.map(toView);
    }
    async addSong(bandId, playlistId, dto) {
        await this.#findOwned(bandId, playlistId);
        const lastPos = await this.prisma.playlistSong.count({ where: { playlistId } });
        const row = await this.prisma.playlistSong.create({
            data: {
                playlistId,
                songId: dto.songId ?? null,
                position: lastPos,
                type: dto.type ?? 'song',
                title: dto.title ?? null,
                setlistName: dto.setlistName ?? null,
                joinWithNext: dto.joinWithNext ?? false,
            },
            include: SONG_INCLUDE,
        });
        return toView(row);
    }
    async updateSong(bandId, playlistId, entryId, dto) {
        await this.#findOwned(bandId, playlistId);
        const row = await this.prisma.playlistSong.update({
            where: { id: entryId },
            data: {
                title: dto.title ?? null,
                setlistName: dto.setlistName ?? null,
                joinWithNext: dto.joinWithNext ?? false,
                type: dto.type,
            },
            include: SONG_INCLUDE,
        });
        return toView(row);
    }
    async removeSong(bandId, playlistId, entryId) {
        await this.#findOwned(bandId, playlistId);
        await this.prisma.playlistSong.delete({ where: { id: entryId } });
        const remaining = await this.prisma.playlistSong.findMany({
            where: { playlistId },
            orderBy: { position: 'asc' },
        });
        for (let i = 0; i < remaining.length; i++) {
            await this.prisma.playlistSong.update({ where: { id: remaining[i].id }, data: { position: i } });
        }
    }
    async reorder(bandId, playlistId, ids) {
        await this.#findOwned(bandId, playlistId);
        for (let i = 0; i < ids.length; i++) {
            await this.prisma.playlistSong.update({ where: { id: ids[i] }, data: { position: i } });
        }
        const rows = await this.prisma.playlistSong.findMany({
            where: { playlistId },
            orderBy: { position: 'asc' },
            include: SONG_INCLUDE,
        });
        return rows.map(toView);
    }
    async #findOwned(bandId, id) {
        const p = await this.prisma.playlist.findFirst({ where: { id, bandId } });
        if (!p)
            throw new common_1.NotFoundException('Playlist not found');
        return p;
    }
};
exports.PlaylistsService = PlaylistsService;
exports.PlaylistsService = PlaylistsService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], PlaylistsService);
//# sourceMappingURL=playlists.service.js.map
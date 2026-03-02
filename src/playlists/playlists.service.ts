import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

const SONG_INCLUDE = {
  song: {
    select: { title: true, artist: true, album: true, duration: true, tempo: true, style: true, notes: true },
  },
} as const;

function toView(ps: any) {
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

@Injectable()
export class PlaylistsService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const playlists = await this.prisma.playlist.findMany({
      where: { bandId },
      orderBy: { createdAt: 'desc' },
      include: { _count: { select: { songs: true } } },
    });
    const withDurations = await Promise.all(
      playlists.map(async p => {
        const songs = await this.prisma.playlistSong.findMany({
          where: { playlistId: p.id },
          include: { song: { select: { duration: true } } },
        });
        const totalDuration = songs.reduce((acc, s) => acc + (s.song?.duration ?? 0), 0);
        return {
          id: p.id,
          name: p.name,
          description: p.description ?? undefined,
          createdAt: p.createdAt.toISOString(),
          songCount: p._count.songs,
          totalDuration,
        };
      }),
    );
    return withDurations;
  }

  async create(bandId: string, dto: { name: string; description?: string }) {
    const p = await this.prisma.playlist.create({ data: { bandId, name: dto.name, description: dto.description } });
    return { id: p.id, name: p.name, description: p.description ?? undefined, createdAt: p.createdAt.toISOString() };
  }

  async update(bandId: string, id: string, dto: { name: string; description?: string }) {
    await this.#findOwned(bandId, id);
    const p = await this.prisma.playlist.update({ where: { id }, data: { name: dto.name, description: dto.description } });
    return { id: p.id, name: p.name, description: p.description ?? undefined, createdAt: p.createdAt.toISOString() };
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.playlist.delete({ where: { id } });
  }

  async getSongs(bandId: string, playlistId: string) {
    await this.#findOwned(bandId, playlistId);
    const rows = await this.prisma.playlistSong.findMany({
      where: { playlistId },
      orderBy: { position: 'asc' },
      include: SONG_INCLUDE,
    });
    return rows.map(toView);
  }

  async addSong(bandId: string, playlistId: string, dto: any) {
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

  async updateSong(bandId: string, playlistId: string, entryId: string, dto: any) {
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

  async removeSong(bandId: string, playlistId: string, entryId: string) {
    await this.#findOwned(bandId, playlistId);
    await this.prisma.playlistSong.delete({ where: { id: entryId } });
    // Re-number positions
    const remaining = await this.prisma.playlistSong.findMany({
      where: { playlistId },
      orderBy: { position: 'asc' },
    });
    for (let i = 0; i < remaining.length; i++) {
      await this.prisma.playlistSong.update({ where: { id: remaining[i].id }, data: { position: i } });
    }
  }

  async reorder(bandId: string, playlistId: string, ids: string[]) {
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

  async #findOwned(bandId: string, id: string) {
    const p = await this.prisma.playlist.findFirst({ where: { id, bandId } });
    if (!p) throw new NotFoundException('Playlist not found');
    return p;
  }
}

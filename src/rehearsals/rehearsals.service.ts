import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class RehearsalsService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
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

  async create(bandId: string, dto: { date: string; notes?: string }) {
    const r = await this.prisma.rehearsal.create({
      data: { bandId, date: dto.date, notes: dto.notes },
      include: { songs: true },
    });
    return { id: r.id, date: r.date, notes: r.notes ?? undefined, createdAt: r.createdAt.toISOString(), songs: [] };
  }

  async update(bandId: string, id: string, dto: { date?: string; notes?: string }) {
    await this.#findOwned(bandId, id);
    const r = await this.prisma.rehearsal.update({ where: { id }, data: dto });
    return { id: r.id, date: r.date, notes: r.notes ?? undefined, createdAt: r.createdAt.toISOString() };
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.rehearsal.delete({ where: { id } });
  }

  async addSong(bandId: string, rehearsalId: string, dto: { songId: string; notes?: string; rating?: number }) {
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

  async updateSong(bandId: string, rehearsalId: string, entryId: string, dto: { notes?: string; rating?: number }) {
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

  async removeSong(bandId: string, rehearsalId: string, entryId: string) {
    await this.#findOwned(bandId, rehearsalId);
    await this.prisma.rehearsalSong.delete({ where: { id: entryId } });
  }

  async #findOwned(bandId: string, id: string) {
    const r = await this.prisma.rehearsal.findFirst({ where: { id, bandId } });
    if (!r) throw new NotFoundException('Rehearsal not found');
    return r;
  }
}

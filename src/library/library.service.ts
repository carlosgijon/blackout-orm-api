import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class LibraryService {
  constructor(private readonly prisma: PrismaService) {}

  findAll(bandId: string) {
    return this.prisma.librarySong.findMany({ where: { bandId }, orderBy: { title: 'asc' } });
  }

  create(bandId: string, dto: any) {
    return this.prisma.librarySong.create({ data: { ...dto, bandId } });
  }

  async update(bandId: string, id: string, dto: any) {
    await this.#findOwned(bandId, id);
    return this.prisma.librarySong.update({ where: { id }, data: dto });
  }

  async remove(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.librarySong.delete({ where: { id } });
  }

  async getStats(bandId: string) {
    const songs = await this.prisma.librarySong.findMany({ where: { bandId } });
    const usageCounts = await this.prisma.playlistSong.groupBy({
      by: ['songId'],
      where: { playlist: { bandId }, songId: { not: null } },
      _count: { songId: true },
    });
    const countMap = new Map(usageCounts.map(u => [u.songId, u._count.songId]));

    const withCount = songs.map(s => ({ ...s, usageCount: countMap.get(s.id) ?? 0 }));

    const byGenre: Record<string, number> = {};
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

  async getUsage(bandId: string, id: string): Promise<string[]> {
    await this.#findOwned(bandId, id);
    const entries = await this.prisma.playlistSong.findMany({
      where: { songId: id },
      include: { playlist: { select: { name: true } } },
    });
    return [...new Set(entries.map(e => e.playlist.name))];
  }

  async #findOwned(bandId: string, id: string) {
    const song = await this.prisma.librarySong.findFirst({ where: { id, bandId } });
    if (!song) throw new NotFoundException('Song not found');
    return song;
  }
}

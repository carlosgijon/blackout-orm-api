import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class VotesService {
  constructor(private prisma: PrismaService) {}

  // ── Session CRUD ───────────────────────────────────────────────────────────

  async getSession(bandId: string, playlistId: string) {
    const session = await this.prisma.voteSession.findFirst({
      where: { bandId, playlistId },
      orderBy: { createdAt: 'desc' },
      include: { votes: { orderBy: { createdAt: 'asc' } } },
    });
    if (!session) return null;
    return this.#formatSession(session);
  }

  async createSession(bandId: string, playlistId: string, title: string) {
    // Verify playlist belongs to band
    const playlist = await this.prisma.playlist.findFirst({ where: { id: playlistId, bandId } });
    if (!playlist) throw new NotFoundException('Playlist not found');

    const session = await this.prisma.voteSession.create({
      data: { bandId, playlistId, title, status: 'open' },
      include: { votes: true },
    });
    return this.#formatSession(session);
  }

  async closeSession(bandId: string, sessionId: string) {
    await this.#findOwned(bandId, sessionId);
    const session = await this.prisma.voteSession.update({
      where: { id: sessionId },
      data: { status: 'closed' },
      include: { votes: { orderBy: { createdAt: 'asc' } } },
    });
    return this.#formatSession(session);
  }

  async reopenSession(bandId: string, sessionId: string) {
    await this.#findOwned(bandId, sessionId);
    const session = await this.prisma.voteSession.update({
      where: { id: sessionId },
      data: { status: 'open' },
      include: { votes: { orderBy: { createdAt: 'asc' } } },
    });
    return this.#formatSession(session);
  }

  async deleteSession(bandId: string, sessionId: string) {
    await this.#findOwned(bandId, sessionId);
    await this.prisma.voteSession.delete({ where: { id: sessionId } });
  }

  // ── Votes ──────────────────────────────────────────────────────────────────

  async castVote(bandId: string, sessionId: string, voterName: string, orderedIds: string[]) {
    const session = await this.#findOwned(bandId, sessionId);
    if (session.status !== 'open') throw new ForbiddenException('Session is closed');

    const vote = await this.prisma.vote.upsert({
      where: { sessionId_voterName: { sessionId, voterName } },
      create: { sessionId, voterName, orderedIds: JSON.stringify(orderedIds) },
      update: { orderedIds: JSON.stringify(orderedIds) },
    });
    return { ...vote, orderedIds };
  }

  async getResults(bandId: string, sessionId: string) {
    await this.#findOwned(bandId, sessionId);
    const session = await this.prisma.voteSession.findUnique({
      where: { id: sessionId },
      include: { votes: true },
    });
    return this.#computeResults(session!.votes);
  }

  // ── Private ────────────────────────────────────────────────────────────────

  async #findOwned(bandId: string, sessionId: string) {
    const session = await this.prisma.voteSession.findFirst({ where: { id: sessionId, bandId } });
    if (!session) throw new NotFoundException('Vote session not found');
    return session;
  }

  #formatSession(session: any) {
    return {
      ...session,
      votes: session.votes.map((v: any) => ({
        ...v,
        orderedIds: JSON.parse(v.orderedIds),
      })),
    };
  }

  #computeResults(votes: any[]): Array<{ songId: string; avgRank: number; voteCount: number }> {
    if (!votes.length) return [];
    const rankSum = new Map<string, number>();
    const rankCount = new Map<string, number>();

    votes.forEach((v) => {
      const ids: string[] = JSON.parse(v.orderedIds);
      ids.forEach((id, idx) => {
        rankSum.set(id, (rankSum.get(id) ?? 0) + idx + 1);
        rankCount.set(id, (rankCount.get(id) ?? 0) + 1);
      });
    });

    return Array.from(rankSum.entries())
      .map(([songId, sum]) => ({
        songId,
        avgRank: sum / (rankCount.get(songId) ?? 1),
        voteCount: rankCount.get(songId) ?? 0,
      }))
      .sort((a, b) => a.avgRank - b.avgRank);
  }
}

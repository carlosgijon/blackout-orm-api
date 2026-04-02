import { Injectable, NotFoundException, ForbiddenException, BadRequestException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

export type PollType   = 'yes_no' | 'approval' | 'proposal';
export type PollStatus = 'draft' | 'open' | 'closed' | 'archived';

@Injectable()
export class PollsService {
  constructor(private prisma: PrismaService) {}

  // ── List ───────────────────────────────────────────────────────────────────

  async list(bandId: string) {
    const polls = await this.prisma.poll.findMany({
      where: { bandId },
      include: {
        options: { orderBy: { createdAt: 'asc' } },
        votes:   { orderBy: { createdAt: 'asc' } },
        gig:     { select: { id: true, title: true } },
      },
      orderBy: { createdAt: 'desc' },
    });
    return polls.map(p => this.#format(p));
  }

  // ── Get one ────────────────────────────────────────────────────────────────

  async get(bandId: string, id: string) {
    const poll = await this.#findOwned(bandId, id);
    return this.#format(poll);
  }

  // ── Create ─────────────────────────────────────────────────────────────────

  async create(bandId: string, dto: {
    title: string;
    description?: string;
    type: PollType;
    createdBy: string;
    deadline?: string;
    linkedGigId?: string;
    options?: string[];   // for approval/yes_no — optional seed options
  }) {
    if (dto.linkedGigId) {
      const gig = await this.prisma.gig.findFirst({ where: { id: dto.linkedGigId, bandId } });
      if (!gig) throw new NotFoundException('Concierto no encontrado');
    }

    const poll = await this.prisma.poll.create({
      data: {
        bandId,
        title:       dto.title,
        description: dto.description,
        type:        dto.type,
        status:      'draft',
        createdBy:   dto.createdBy,
        deadline:    dto.deadline ? new Date(dto.deadline) : undefined,
        linkedGigId: dto.linkedGigId ?? null,
        options: dto.options?.length
          ? { create: dto.options.map(text => ({ text, proposedBy: dto.createdBy })) }
          : undefined,
      },
      include: {
        options: { orderBy: { createdAt: 'asc' } },
        votes:   true,
        gig:     { select: { id: true, title: true } },
      },
    });
    return this.#format(poll);
  }

  // ── Update status ──────────────────────────────────────────────────────────

  async setStatus(bandId: string, id: string, status: PollStatus) {
    await this.#findOwned(bandId, id);
    const poll = await this.prisma.poll.update({
      where: { id },
      data:  { status },
      include: {
        options: { orderBy: { createdAt: 'asc' } },
        votes:   { orderBy: { createdAt: 'asc' } },
        gig:     { select: { id: true, title: true } },
      },
    });
    return this.#format(poll);
  }

  // ── Delete ─────────────────────────────────────────────────────────────────

  async delete(bandId: string, id: string) {
    await this.#findOwned(bandId, id);
    await this.prisma.poll.delete({ where: { id } });
  }

  // ── Options ────────────────────────────────────────────────────────────────

  async addOption(bandId: string, pollId: string, text: string, proposedBy: string) {
    const poll = await this.#findOwned(bandId, pollId);
    if (poll.status === 'closed' || poll.status === 'archived') {
      throw new ForbiddenException('No se pueden añadir opciones a una votación cerrada');
    }
    if (poll.type === 'yes_no') {
      throw new BadRequestException('Las votaciones Sí/No no tienen opciones');
    }
    const option = await this.prisma.pollOption.create({
      data: { pollId, text, proposedBy },
    });
    return option;
  }

  async deleteOption(bandId: string, pollId: string, optionId: string) {
    const poll = await this.#findOwned(bandId, pollId);
    if (poll.status === 'open') {
      throw new ForbiddenException('No se pueden eliminar opciones con la votación abierta');
    }
    await this.prisma.pollOption.deleteMany({ where: { id: optionId, pollId } });
  }

  // ── Votes ──────────────────────────────────────────────────────────────────

  /** Cast or update a vote */
  async castVote(bandId: string, pollId: string, dto: {
    voterName: string;
    // yes_no: value = 'yes'|'no'|'abstain'
    // approval/proposal: approvedOptionIds = string[]
    value?: string;
    approvedOptionIds?: string[];
    comment?: string;
  }) {
    const poll = await this.#findOwned(bandId, pollId);
    if (poll.status !== 'open') throw new ForbiddenException('La votación no está abierta');

    if (poll.type === 'yes_no') {
      if (!dto.value || !['yes', 'no', 'abstain'].includes(dto.value)) {
        throw new BadRequestException('Valor inválido. Usa: yes | no | abstain');
      }
      // Delete previous vote from this voter, then create
      await this.prisma.pollVote.deleteMany({ where: { pollId, voterName: dto.voterName, optionId: null } });
      await this.prisma.pollVote.create({
        data: { pollId, voterName: dto.voterName, value: dto.value, comment: dto.comment },
      });
    } else {
      // approval / proposal — replace all votes from this voter
      await this.prisma.pollVote.deleteMany({ where: { pollId, voterName: dto.voterName } });
      if (dto.approvedOptionIds?.length) {
        await this.prisma.pollVote.createMany({
          data: dto.approvedOptionIds.map(optionId => ({
            pollId,
            optionId,
            voterName: dto.voterName,
            value: 'approve',
            comment: dto.comment,
          })),
        });
      }
    }

    return this.get(bandId, pollId);
  }

  // ── Results ────────────────────────────────────────────────────────────────

  async getResults(bandId: string, pollId: string) {
    const poll = await this.#findOwned(bandId, pollId);
    const formatted = this.#format(poll);

    if (poll.type === 'yes_no') {
      const yes     = poll.votes.filter((v: any) => v.value === 'yes').length;
      const no      = poll.votes.filter((v: any) => v.value === 'no').length;
      const abstain = poll.votes.filter((v: any) => v.value === 'abstain').length;
      const total   = yes + no + abstain;
      return {
        type: 'yes_no',
        total,
        yes,     yesPct:     total ? Math.round((yes / total) * 100) : 0,
        no,      noPct:      total ? Math.round((no / total) * 100) : 0,
        abstain, abstainPct: total ? Math.round((abstain / total) * 100) : 0,
        voters: poll.votes.map((v: any) => ({ voterName: v.voterName, value: v.value, comment: v.comment })),
      };
    } else {
      // approval / proposal
      const optionResults = formatted.options.map((opt: any) => {
        const voteCount = poll.votes.filter((v: any) => v.optionId === opt.id).length;
        return { ...opt, voteCount };
      }).sort((a: any, b: any) => b.voteCount - a.voteCount);

      const totalVoters = new Set(poll.votes.map((v: any) => v.voterName)).size;
      return { type: poll.type, totalVoters, options: optionResults };
    }
  }

  // ── Private ────────────────────────────────────────────────────────────────

  async #findOwned(bandId: string, id: string) {
    const poll = await this.prisma.poll.findFirst({
      where: { id, bandId },
      include: {
        options: { orderBy: { createdAt: 'asc' } },
        votes:   { orderBy: { createdAt: 'asc' } },
        gig:     { select: { id: true, title: true } },
      },
    });
    if (!poll) throw new NotFoundException('Votación no encontrada');
    return poll;
  }

  #format(poll: any) {
    return {
      id:          poll.id,
      bandId:      poll.bandId,
      title:       poll.title,
      description: poll.description ?? null,
      type:        poll.type,
      status:      poll.status,
      createdBy:   poll.createdBy,
      deadline:    poll.deadline ? poll.deadline.toISOString().slice(0, 10) : null,
      linkedGigId: poll.linkedGigId ?? null,
      linkedGig:   poll.gig ?? null,
      createdAt:   poll.createdAt,
      options:     poll.options,
      votes:       poll.votes,
      voteCount:   new Set(poll.votes.map((v: any) => v.voterName)).size,
    };
  }
}

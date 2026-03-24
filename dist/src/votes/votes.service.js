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
exports.VotesService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let VotesService = class VotesService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async getSession(bandId, playlistId) {
        const session = await this.prisma.voteSession.findFirst({
            where: { bandId, playlistId },
            orderBy: { createdAt: 'desc' },
            include: { votes: { orderBy: { createdAt: 'asc' } } },
        });
        if (!session)
            return null;
        return this.#formatSession(session);
    }
    async createSession(bandId, playlistId, title) {
        const playlist = await this.prisma.playlist.findFirst({ where: { id: playlistId, bandId } });
        if (!playlist)
            throw new common_1.NotFoundException('Playlist not found');
        const session = await this.prisma.voteSession.create({
            data: { bandId, playlistId, title, status: 'open' },
            include: { votes: true },
        });
        return this.#formatSession(session);
    }
    async closeSession(bandId, sessionId) {
        await this.#findOwned(bandId, sessionId);
        const session = await this.prisma.voteSession.update({
            where: { id: sessionId },
            data: { status: 'closed' },
            include: { votes: { orderBy: { createdAt: 'asc' } } },
        });
        return this.#formatSession(session);
    }
    async reopenSession(bandId, sessionId) {
        await this.#findOwned(bandId, sessionId);
        const session = await this.prisma.voteSession.update({
            where: { id: sessionId },
            data: { status: 'open' },
            include: { votes: { orderBy: { createdAt: 'asc' } } },
        });
        return this.#formatSession(session);
    }
    async deleteSession(bandId, sessionId) {
        await this.#findOwned(bandId, sessionId);
        await this.prisma.voteSession.delete({ where: { id: sessionId } });
    }
    async castVote(bandId, sessionId, voterName, orderedIds) {
        const session = await this.#findOwned(bandId, sessionId);
        if (session.status !== 'open')
            throw new common_1.ForbiddenException('Session is closed');
        const vote = await this.prisma.vote.upsert({
            where: { sessionId_voterName: { sessionId, voterName } },
            create: { sessionId, voterName, orderedIds: JSON.stringify(orderedIds) },
            update: { orderedIds: JSON.stringify(orderedIds) },
        });
        return { ...vote, orderedIds };
    }
    async getResults(bandId, sessionId) {
        await this.#findOwned(bandId, sessionId);
        const session = await this.prisma.voteSession.findUnique({
            where: { id: sessionId },
            include: { votes: true },
        });
        return this.#computeResults(session.votes);
    }
    async #findOwned(bandId, sessionId) {
        const session = await this.prisma.voteSession.findFirst({ where: { id: sessionId, bandId } });
        if (!session)
            throw new common_1.NotFoundException('Vote session not found');
        return session;
    }
    #formatSession(session) {
        return {
            ...session,
            votes: session.votes.map((v) => ({
                ...v,
                orderedIds: JSON.parse(v.orderedIds),
            })),
        };
    }
    #computeResults(votes) {
        if (!votes.length)
            return [];
        const rankSum = new Map();
        const rankCount = new Map();
        votes.forEach((v) => {
            const ids = JSON.parse(v.orderedIds);
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
};
exports.VotesService = VotesService;
exports.VotesService = VotesService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], VotesService);
//# sourceMappingURL=votes.service.js.map
import { PrismaService } from '../prisma/prisma.service';
export declare class VotesService {
    #private;
    private prisma;
    constructor(prisma: PrismaService);
    getSession(bandId: string, playlistId: string): Promise<any>;
    createSession(bandId: string, playlistId: string, title: string): Promise<any>;
    closeSession(bandId: string, sessionId: string): Promise<any>;
    reopenSession(bandId: string, sessionId: string): Promise<any>;
    deleteSession(bandId: string, sessionId: string): Promise<void>;
    castVote(bandId: string, sessionId: string, voterName: string, orderedIds: string[]): Promise<{
        orderedIds: string[];
        id: string;
        createdAt: Date;
        sessionId: string;
        voterName: string;
    }>;
    getResults(bandId: string, sessionId: string): Promise<{
        songId: string;
        avgRank: number;
        voteCount: number;
    }[]>;
}

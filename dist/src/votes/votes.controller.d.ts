import { VotesService } from './votes.service';
export declare class VotesController {
    private readonly votesService;
    constructor(votesService: VotesService);
    getSession(req: any, playlistId: string): Promise<any>;
    createSession(req: any, body: {
        playlistId: string;
        title: string;
    }): Promise<any>;
    closeSession(req: any, id: string): Promise<any>;
    reopenSession(req: any, id: string): Promise<any>;
    deleteSession(req: any, id: string): Promise<void>;
    castVote(req: any, id: string, body: {
        voterName: string;
        orderedIds: string[];
    }): Promise<{
        orderedIds: string[];
        id: string;
        createdAt: Date;
        sessionId: string;
        voterName: string;
    }>;
    getResults(req: any, id: string): Promise<{
        songId: string;
        avgRank: number;
        voteCount: number;
    }[]>;
}

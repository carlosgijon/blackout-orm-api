import { PrismaService } from '../prisma/prisma.service';
export declare class RehearsalsService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
        id: string;
        date: string;
        notes: string | undefined;
        createdAt: string;
        songs: {
            id: string;
            songId: string;
            title: string;
            artist: string;
            tempo: number | undefined;
            style: string | undefined;
            notes: string | undefined;
            rating: number | undefined;
        }[];
    }[]>;
    create(bandId: string, dto: {
        date: string;
        notes?: string;
    }): Promise<{
        id: string;
        date: string;
        notes: string | undefined;
        createdAt: string;
        songs: never[];
    }>;
    update(bandId: string, id: string, dto: {
        date?: string;
        notes?: string;
    }): Promise<{
        id: string;
        date: string;
        notes: string | undefined;
        createdAt: string;
    }>;
    remove(bandId: string, id: string): Promise<void>;
    addSong(bandId: string, rehearsalId: string, dto: {
        songId: string;
        notes?: string;
        rating?: number;
    }): Promise<{
        id: string;
        songId: string;
        title: string;
        artist: string;
        tempo: number | undefined;
        style: string | undefined;
        notes: string | undefined;
        rating: number | undefined;
    }>;
    updateSong(bandId: string, rehearsalId: string, entryId: string, dto: {
        notes?: string;
        rating?: number;
    }): Promise<{
        id: string;
        songId: string;
        title: string;
        artist: string;
        tempo: number | undefined;
        style: string | undefined;
        notes: string | undefined;
        rating: number | undefined;
    }>;
    removeSong(bandId: string, rehearsalId: string, entryId: string): Promise<void>;
}

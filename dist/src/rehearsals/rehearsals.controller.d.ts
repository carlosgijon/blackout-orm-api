import { RehearsalsService } from './rehearsals.service';
export declare class RehearsalsController {
    private readonly svc;
    constructor(svc: RehearsalsService);
    findAll(user: any): Promise<{
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
    create(user: any, dto: any): Promise<{
        id: string;
        date: string;
        notes: string | undefined;
        createdAt: string;
        songs: never[];
    }>;
    update(user: any, id: string, dto: any): Promise<{
        id: string;
        date: string;
        notes: string | undefined;
        createdAt: string;
    }>;
    remove(user: any, id: string): Promise<void>;
    addSong(user: any, id: string, dto: any): Promise<{
        id: string;
        songId: string;
        title: string;
        artist: string;
        tempo: number | undefined;
        style: string | undefined;
        notes: string | undefined;
        rating: number | undefined;
    }>;
    updateSong(user: any, id: string, entryId: string, dto: any): Promise<{
        id: string;
        songId: string;
        title: string;
        artist: string;
        tempo: number | undefined;
        style: string | undefined;
        notes: string | undefined;
        rating: number | undefined;
    }>;
    removeSong(user: any, id: string, entryId: string): Promise<void>;
}

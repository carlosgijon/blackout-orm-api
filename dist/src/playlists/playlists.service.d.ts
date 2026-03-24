import { PrismaService } from '../prisma/prisma.service';
export declare class PlaylistsService {
    #private;
    private readonly prisma;
    getGigs(bandId: string, playlistId: string): Promise<{
        id: string;
        title: string;
        date: string | undefined;
        status: string;
        venueName: string | undefined;
    }[]>;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
        id: string;
        name: string;
        description: string | undefined;
        createdAt: string;
        songCount: number;
        totalDuration: number;
    }[]>;
    create(bandId: string, dto: {
        name: string;
        description?: string;
    }): Promise<{
        id: string;
        name: string;
        description: string | undefined;
        createdAt: string;
    }>;
    update(bandId: string, id: string, dto: {
        name: string;
        description?: string;
    }): Promise<{
        id: string;
        name: string;
        description: string | undefined;
        createdAt: string;
    }>;
    remove(bandId: string, id: string): Promise<void>;
    getSongs(bandId: string, playlistId: string): Promise<{
        id: any;
        playlistId: any;
        songId: any;
        position: any;
        type: any;
        title: any;
        setlistName: any;
        joinWithNext: any;
        artist: any;
        album: any;
        duration: any;
        tempo: any;
        style: any;
        notes: any;
    }[]>;
    addSong(bandId: string, playlistId: string, dto: any): Promise<{
        id: any;
        playlistId: any;
        songId: any;
        position: any;
        type: any;
        title: any;
        setlistName: any;
        joinWithNext: any;
        artist: any;
        album: any;
        duration: any;
        tempo: any;
        style: any;
        notes: any;
    }>;
    updateSong(bandId: string, playlistId: string, entryId: string, dto: any): Promise<{
        id: any;
        playlistId: any;
        songId: any;
        position: any;
        type: any;
        title: any;
        setlistName: any;
        joinWithNext: any;
        artist: any;
        album: any;
        duration: any;
        tempo: any;
        style: any;
        notes: any;
    }>;
    removeSong(bandId: string, playlistId: string, entryId: string): Promise<void>;
    reorder(bandId: string, playlistId: string, ids: string[]): Promise<{
        id: any;
        playlistId: any;
        songId: any;
        position: any;
        type: any;
        title: any;
        setlistName: any;
        joinWithNext: any;
        artist: any;
        album: any;
        duration: any;
        tempo: any;
        style: any;
        notes: any;
    }[]>;
}

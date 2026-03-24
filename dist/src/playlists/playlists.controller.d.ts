import { PlaylistsService } from './playlists.service';
export declare class PlaylistsController {
    private readonly playlists;
    constructor(playlists: PlaylistsService);
    findAll(user: any): Promise<{
        id: string;
        name: string;
        description: string | undefined;
        createdAt: string;
        songCount: number;
        totalDuration: number;
    }[]>;
    create(user: any, dto: any): Promise<{
        id: string;
        name: string;
        description: string | undefined;
        createdAt: string;
    }>;
    update(user: any, id: string, dto: any): Promise<{
        id: string;
        name: string;
        description: string | undefined;
        createdAt: string;
    }>;
    remove(user: any, id: string): Promise<void>;
    getSongs(user: any, id: string): Promise<{
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
    addSong(user: any, id: string, dto: any): Promise<{
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
    updateSong(user: any, id: string, entryId: string, dto: any): Promise<{
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
    removeSong(user: any, id: string, entryId: string): Promise<void>;
    reorder(user: any, id: string, dto: {
        ids: string[];
    }): Promise<{
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
    getGigs(user: any, id: string): Promise<{
        id: string;
        title: string;
        date: string | undefined;
        status: string;
        venueName: string | undefined;
    }[]>;
}

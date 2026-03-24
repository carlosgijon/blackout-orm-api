import { LibraryService } from './library.service';
export declare class LibraryController {
    private readonly library;
    constructor(library: LibraryService);
    findAll(user: any): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        title: string;
        artist: string;
        album: string | null;
        duration: number | null;
        tempo: number | null;
        style: string | null;
        notes: string | null;
        startNote: string | null;
        endNote: string | null;
    }[]>;
    create(user: any, dto: any): import(".prisma/client").Prisma.Prisma__LibrarySongClient<{
        id: string;
        bandId: string;
        title: string;
        artist: string;
        album: string | null;
        duration: number | null;
        tempo: number | null;
        style: string | null;
        notes: string | null;
        startNote: string | null;
        endNote: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(user: any, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        title: string;
        artist: string;
        album: string | null;
        duration: number | null;
        tempo: number | null;
        style: string | null;
        notes: string | null;
        startNote: string | null;
        endNote: string | null;
    }>;
    remove(user: any, id: string): Promise<void>;
    getStats(user: any): Promise<{
        totalSongs: number;
        totalWithTempo: number;
        totalWithDuration: number;
        byGenre: Record<string, number>;
        mostUsed: {
            usageCount: number;
            id: string;
            bandId: string;
            title: string;
            artist: string;
            album: string | null;
            duration: number | null;
            tempo: number | null;
            style: string | null;
            notes: string | null;
            startNote: string | null;
            endNote: string | null;
        }[];
        neverUsed: {
            usageCount: number;
            id: string;
            bandId: string;
            title: string;
            artist: string;
            album: string | null;
            duration: number | null;
            tempo: number | null;
            style: string | null;
            notes: string | null;
            startNote: string | null;
            endNote: string | null;
        }[];
    }>;
    getUsage(user: any, id: string): Promise<string[]>;
}

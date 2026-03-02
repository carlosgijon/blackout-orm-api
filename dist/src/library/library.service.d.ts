import { PrismaService } from '../prisma/prisma.service';
export declare class LibraryService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        title: string;
        artist: string;
        album: string | null;
        duration: number | null;
        tempo: number | null;
        style: string | null;
        notes: string | null;
    }[]>;
    create(bandId: string, dto: any): import(".prisma/client").Prisma.Prisma__LibrarySongClient<{
        id: string;
        bandId: string;
        title: string;
        artist: string;
        album: string | null;
        duration: number | null;
        tempo: number | null;
        style: string | null;
        notes: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        title: string;
        artist: string;
        album: string | null;
        duration: number | null;
        tempo: number | null;
        style: string | null;
        notes: string | null;
    }>;
    remove(bandId: string, id: string): Promise<void>;
    getUsage(bandId: string, id: string): Promise<string[]>;
}

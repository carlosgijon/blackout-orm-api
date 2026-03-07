import { PrismaService } from '../prisma/prisma.service';
export declare class InstrumentsService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        ampId: string | null;
        channelOrder: number;
    }[]>;
    create(bandId: string, dto: any): import(".prisma/client").Prisma.Prisma__InstrumentClient<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        ampId: string | null;
        channelOrder: number;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        ampId: string | null;
        channelOrder: number;
    }>;
    remove(bandId: string, id: string): Promise<void>;
    setMics(bandId: string, instrumentId: string, micIds: string[]): Promise<void>;
}

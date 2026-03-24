import { InstrumentsService } from './instruments.service';
export declare class InstrumentsController {
    private readonly instruments;
    constructor(instruments: InstrumentsService);
    findAll(user: any): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        name: string;
        type: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        ampId: string | null;
        channelOrder: number;
    }[]>;
    create(user: any, dto: any): import(".prisma/client").Prisma.Prisma__InstrumentClient<{
        id: string;
        bandId: string;
        name: string;
        type: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        ampId: string | null;
        channelOrder: number;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(user: any, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        name: string;
        type: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        ampId: string | null;
        channelOrder: number;
    }>;
    remove(user: any, id: string): Promise<void>;
    setMics(user: any, id: string, dto: {
        micIds: string[];
    }): Promise<void>;
}

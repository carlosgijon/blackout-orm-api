import { PrismaService } from '../prisma/prisma.service';
export declare class MicrophonesService {
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
        polarPattern: string | null;
        phantomPower: boolean;
        monoStereo: string;
        usage: string | null;
        assignedToType: string | null;
        assignedToId: string | null;
    }[]>;
    create(bandId: string, dto: any): import(".prisma/client").Prisma.Prisma__MicrophoneClient<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        brand: string | null;
        model: string | null;
        polarPattern: string | null;
        phantomPower: boolean;
        monoStereo: string;
        usage: string | null;
        assignedToType: string | null;
        assignedToId: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        brand: string | null;
        model: string | null;
        polarPattern: string | null;
        phantomPower: boolean;
        monoStereo: string;
        usage: string | null;
        assignedToType: string | null;
        assignedToId: string | null;
    }>;
    remove(bandId: string, id: string): Promise<void>;
}

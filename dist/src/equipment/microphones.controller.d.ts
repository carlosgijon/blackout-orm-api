import { MicrophonesService } from './microphones.service';
export declare class MicrophonesController {
    private readonly mics;
    constructor(mics: MicrophonesService);
    findAll(user: any): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        name: string;
        type: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        polarPattern: string | null;
        phantomPower: boolean;
        monoStereo: string;
        usage: string | null;
        assignedToType: string | null;
        assignedToId: string | null;
    }[]>;
    create(user: any, dto: any): import(".prisma/client").Prisma.Prisma__MicrophoneClient<{
        id: string;
        bandId: string;
        name: string;
        type: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        polarPattern: string | null;
        phantomPower: boolean;
        monoStereo: string;
        usage: string | null;
        assignedToType: string | null;
        assignedToId: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(user: any, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        name: string;
        type: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        polarPattern: string | null;
        phantomPower: boolean;
        monoStereo: string;
        usage: string | null;
        assignedToType: string | null;
        assignedToId: string | null;
    }>;
    remove(user: any, id: string): Promise<void>;
}

import { AmplifiersService } from './amplifiers.service';
export declare class AmplifiersController {
    private readonly amplifiers;
    constructor(amplifiers: AmplifiersService);
    findAll(user: any): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        stagePosition: string | null;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        wattage: number | null;
        cabinetBrand: string | null;
        speakerBrand: string | null;
        speakerModel: string | null;
        speakerConfig: string | null;
    }[]>;
    create(user: any, dto: any): import(".prisma/client").Prisma.Prisma__AmplifierClient<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        stagePosition: string | null;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        wattage: number | null;
        cabinetBrand: string | null;
        speakerBrand: string | null;
        speakerModel: string | null;
        speakerConfig: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(user: any, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        type: string;
        stagePosition: string | null;
        brand: string | null;
        model: string | null;
        monoStereo: string | null;
        memberId: string | null;
        routing: string;
        wattage: number | null;
        cabinetBrand: string | null;
        speakerBrand: string | null;
        speakerModel: string | null;
        speakerConfig: string | null;
    }>;
    remove(user: any, id: string): Promise<void>;
    updateInstrumentLink(user: any, id: string, dto: {
        instrumentId: string | null;
    }): Promise<void>;
    setMics(user: any, id: string, dto: {
        micIds: string[];
    }): Promise<void>;
}

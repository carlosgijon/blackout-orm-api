import { PrismaService } from '../prisma/prisma.service';
export declare class PaService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        wattage: number | null;
        category: string;
        quantity: number;
        channels: number | null;
        auxSends: number | null;
        monitorType: string | null;
        iemWireless: boolean;
    }[]>;
    create(bandId: string, dto: any): import(".prisma/client").Prisma.Prisma__PaEquipmentClient<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        wattage: number | null;
        category: string;
        quantity: number;
        channels: number | null;
        auxSends: number | null;
        monitorType: string | null;
        iemWireless: boolean;
    }, never, import("@prisma/client/runtime/library").DefaultArgs>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        brand: string | null;
        model: string | null;
        wattage: number | null;
        category: string;
        quantity: number;
        channels: number | null;
        auxSends: number | null;
        monitorType: string | null;
        iemWireless: boolean;
    }>;
    remove(bandId: string, id: string): Promise<void>;
}

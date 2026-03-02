import { PaService } from './pa.service';
export declare class PaController {
    private readonly pa;
    constructor(pa: PaService);
    findAll(user: any): import(".prisma/client").Prisma.PrismaPromise<{
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
    create(user: any, dto: any): import(".prisma/client").Prisma.Prisma__PaEquipmentClient<{
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
    update(user: any, id: string, dto: any): Promise<{
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
    remove(user: any, id: string): Promise<void>;
}

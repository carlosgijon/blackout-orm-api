import { PrismaService } from '../prisma/prisma.service';
export declare class MerchService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
        id: any;
        name: any;
        type: any;
        productionCost: any;
        batchSize: any;
        sellingPrice: any;
        fixedCosts: any;
        notes: any;
        createdAt: any;
    }[]>;
    create(bandId: string, dto: any): Promise<{
        id: any;
        name: any;
        type: any;
        productionCost: any;
        batchSize: any;
        sellingPrice: any;
        fixedCosts: any;
        notes: any;
        createdAt: any;
    }>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: any;
        name: any;
        type: any;
        productionCost: any;
        batchSize: any;
        sellingPrice: any;
        fixedCosts: any;
        notes: any;
        createdAt: any;
    }>;
    remove(bandId: string, id: string): Promise<void>;
}

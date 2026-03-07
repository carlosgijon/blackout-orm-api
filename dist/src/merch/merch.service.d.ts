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
        stock: any;
        hasSizes: any;
        stockSizes: Record<string, number> | undefined;
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
        stock: any;
        hasSizes: any;
        stockSizes: Record<string, number> | undefined;
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
        stock: any;
        hasSizes: any;
        stockSizes: Record<string, number> | undefined;
        notes: any;
        createdAt: any;
    }>;
    remove(bandId: string, id: string): Promise<void>;
    restock(bandId: string, id: string, dto: {
        stock?: number;
        stockSizes?: Record<string, number>;
    }): Promise<{
        id: any;
        name: any;
        type: any;
        productionCost: any;
        batchSize: any;
        sellingPrice: any;
        fixedCosts: any;
        stock: any;
        hasSizes: any;
        stockSizes: Record<string, number> | undefined;
        notes: any;
        createdAt: any;
    }>;
    sell(bandId: string, id: string, dto: {
        quantity: number;
        unitPrice: number;
        date: string;
        size?: string;
        notes?: string;
    }): Promise<{
        item: {
            id: any;
            name: any;
            type: any;
            productionCost: any;
            batchSize: any;
            sellingPrice: any;
            fixedCosts: any;
            stock: any;
            hasSizes: any;
            stockSizes: Record<string, number> | undefined;
            notes: any;
            createdAt: any;
        };
        transaction: {
            id: string;
            type: string;
            category: string;
            amount: number;
            date: string;
            description: string | null;
            createdAt: string;
        };
    }>;
}

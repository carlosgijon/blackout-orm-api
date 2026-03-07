import { MerchService } from './merch.service';
export declare class MerchController {
    private readonly merch;
    constructor(merch: MerchService);
    findAll(user: any): Promise<{
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
    create(user: any, dto: any): Promise<{
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
    update(user: any, id: string, dto: any): Promise<{
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
    remove(user: any, id: string): Promise<void>;
}

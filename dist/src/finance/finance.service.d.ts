import { PrismaService } from '../prisma/prisma.service';
export declare class FinanceService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAllTransactions(bandId: string): Promise<{
        id: any;
        type: any;
        category: any;
        amount: any;
        date: any;
        description: any;
        gigId: any;
        createdAt: any;
    }[]>;
    createTransaction(bandId: string, dto: any): Promise<{
        id: any;
        type: any;
        category: any;
        amount: any;
        date: any;
        description: any;
        gigId: any;
        createdAt: any;
    }>;
    updateTransaction(bandId: string, id: string, dto: any): Promise<{
        id: any;
        type: any;
        category: any;
        amount: any;
        date: any;
        description: any;
        gigId: any;
        createdAt: any;
    }>;
    removeTransaction(bandId: string, id: string): Promise<void>;
    findAllWishList(bandId: string): Promise<{
        id: any;
        name: any;
        category: any;
        estimatedPrice: any;
        priority: any;
        notes: any;
        purchased: any;
        createdAt: any;
    }[]>;
    createWishListItem(bandId: string, dto: any): Promise<{
        id: any;
        name: any;
        category: any;
        estimatedPrice: any;
        priority: any;
        notes: any;
        purchased: any;
        createdAt: any;
    }>;
    updateWishListItem(bandId: string, id: string, dto: any): Promise<{
        id: any;
        name: any;
        category: any;
        estimatedPrice: any;
        priority: any;
        notes: any;
        purchased: any;
        createdAt: any;
    }>;
    removeWishListItem(bandId: string, id: string): Promise<void>;
}

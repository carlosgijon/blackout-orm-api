import { FinanceService } from './finance.service';
export declare class FinanceController {
    private readonly finance;
    constructor(finance: FinanceService);
    findAllTransactions(user: any): Promise<{
        id: any;
        type: any;
        category: any;
        amount: any;
        date: any;
        description: any;
        gigId: any;
        createdAt: any;
    }[]>;
    createTransaction(user: any, dto: any): Promise<{
        id: any;
        type: any;
        category: any;
        amount: any;
        date: any;
        description: any;
        gigId: any;
        createdAt: any;
    }>;
    updateTransaction(user: any, id: string, dto: any): Promise<{
        id: any;
        type: any;
        category: any;
        amount: any;
        date: any;
        description: any;
        gigId: any;
        createdAt: any;
    }>;
    removeTransaction(user: any, id: string): Promise<void>;
    findAllWishList(user: any): Promise<{
        id: any;
        name: any;
        category: any;
        estimatedPrice: any;
        priority: any;
        notes: any;
        purchased: any;
        createdAt: any;
    }[]>;
    createWishListItem(user: any, dto: any): Promise<{
        id: any;
        name: any;
        category: any;
        estimatedPrice: any;
        priority: any;
        notes: any;
        purchased: any;
        createdAt: any;
    }>;
    updateWishListItem(user: any, id: string, dto: any): Promise<{
        id: any;
        name: any;
        category: any;
        estimatedPrice: any;
        priority: any;
        notes: any;
        purchased: any;
        createdAt: any;
    }>;
    removeWishListItem(user: any, id: string): Promise<void>;
}

import { GigsService } from './gigs.service';
export declare class GigsController {
    private readonly gigs;
    constructor(gigs: GigsService);
    findAll(user: any): Promise<{
        id: any;
        venueId: any;
        venueName: any;
        setlistId: any;
        title: any;
        date: any;
        time: any;
        status: any;
        pay: any;
        loadInTime: any;
        soundcheckTime: any;
        setTime: any;
        notes: any;
        attendance: any;
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }[]>;
    getSummary(user: any, id: string): Promise<{
        gig: {
            id: any;
            venueId: any;
            venueName: any;
            setlistId: any;
            title: any;
            date: any;
            time: any;
            status: any;
            pay: any;
            loadInTime: any;
            soundcheckTime: any;
            setTime: any;
            notes: any;
            attendance: any;
            followUpDate: any;
            followUpNote: any;
            createdAt: any;
        };
        transactions: {
            id: string;
            type: string;
            category: string;
            amount: number;
            date: string;
            description: string | undefined;
            gigId: string | undefined;
            createdAt: string;
        }[];
        merchSales: {
            id: string;
            amount: number;
            date: string;
            description: string | undefined;
            createdAt: string;
        }[];
    }>;
    create(user: any, dto: any): Promise<{
        id: any;
        venueId: any;
        venueName: any;
        setlistId: any;
        title: any;
        date: any;
        time: any;
        status: any;
        pay: any;
        loadInTime: any;
        soundcheckTime: any;
        setTime: any;
        notes: any;
        attendance: any;
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }>;
    update(user: any, id: string, dto: any): Promise<{
        id: any;
        venueId: any;
        venueName: any;
        setlistId: any;
        title: any;
        date: any;
        time: any;
        status: any;
        pay: any;
        loadInTime: any;
        soundcheckTime: any;
        setTime: any;
        notes: any;
        attendance: any;
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }>;
    updateStatus(user: any, id: string, dto: {
        status: string;
    }): Promise<{
        id: any;
        venueId: any;
        venueName: any;
        setlistId: any;
        title: any;
        date: any;
        time: any;
        status: any;
        pay: any;
        loadInTime: any;
        soundcheckTime: any;
        setTime: any;
        notes: any;
        attendance: any;
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }>;
    updateFollowUp(user: any, id: string, dto: any): Promise<void>;
    remove(user: any, id: string): Promise<void>;
    getContacts(user: any, gigId: string): Promise<{
        createdAt: string;
        id: string;
        date: string;
        notes: string | null;
        gigId: string;
        contactType: string;
    }[]>;
    createContact(user: any, gigId: string, dto: any): Promise<{
        createdAt: string;
        id: string;
        date: string;
        notes: string | null;
        gigId: string;
        contactType: string;
    }>;
    removeContact(user: any, gigId: string, contactId: string): Promise<void>;
    getChecklists(user: any, gigId: string): Promise<{
        createdAt: string;
        id: string;
        name: string;
        gigId: string;
    }[]>;
    createChecklist(user: any, gigId: string, dto: {
        name: string;
    }): Promise<{
        createdAt: string;
        id: string;
        name: string;
        gigId: string;
    }>;
    removeChecklist(user: any, gigId: string, checklistId: string): Promise<void>;
    getItems(user: any, checklistId: string): Promise<{
        id: string;
        category: string;
        checklistId: string;
        text: string;
        done: boolean;
        sortOrder: number;
    }[]>;
    createItem(user: any, checklistId: string, dto: any): Promise<{
        id: string;
        category: string;
        checklistId: string;
        text: string;
        done: boolean;
        sortOrder: number;
    }>;
    updateItem(user: any, checklistId: string, itemId: string, dto: any): Promise<{
        id: string;
        category: string;
        checklistId: string;
        text: string;
        done: boolean;
        sortOrder: number;
    }>;
    removeItem(user: any, checklistId: string, itemId: string): Promise<void>;
    resetItems(user: any, checklistId: string): Promise<void>;
    removeContactById(user: any, contactId: string): Promise<void>;
    removeChecklistById(user: any, checklistId: string): Promise<void>;
    removeItemById(user: any, itemId: string): Promise<void>;
}

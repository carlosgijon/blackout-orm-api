import { PrismaService } from '../prisma/prisma.service';
export declare class GigsService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
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
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }[]>;
    create(bandId: string, dto: any): Promise<{
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
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }>;
    update(bandId: string, id: string, dto: any): Promise<{
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
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }>;
    updateStatus(bandId: string, id: string, status: string): Promise<{
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
        followUpDate: any;
        followUpNote: any;
        createdAt: any;
    }>;
    updateFollowUp(bandId: string, id: string, dto: {
        followUpDate?: string;
        followUpNote?: string;
    }): Promise<void>;
    remove(bandId: string, id: string): Promise<void>;
    getContacts(bandId: string, gigId: string): Promise<{
        createdAt: string;
        id: string;
        notes: string | null;
        date: string;
        gigId: string;
        contactType: string;
    }[]>;
    createContact(bandId: string, gigId: string, dto: any): Promise<{
        createdAt: string;
        id: string;
        notes: string | null;
        date: string;
        gigId: string;
        contactType: string;
    }>;
    removeContact(bandId: string, gigId: string, contactId: string): Promise<void>;
    getChecklists(bandId: string, gigId: string): Promise<{
        createdAt: string;
        id: string;
        name: string;
        gigId: string;
    }[]>;
    createChecklist(bandId: string, gigId: string, dto: {
        name: string;
    }): Promise<{
        createdAt: string;
        id: string;
        name: string;
        gigId: string;
    }>;
    removeChecklist(bandId: string, gigId: string, checklistId: string): Promise<void>;
    getItems(bandId: string, checklistId: string): Promise<{
        id: string;
        sortOrder: number;
        category: string;
        checklistId: string;
        text: string;
        done: boolean;
    }[]>;
    createItem(bandId: string, checklistId: string, dto: any): Promise<{
        id: string;
        sortOrder: number;
        category: string;
        checklistId: string;
        text: string;
        done: boolean;
    }>;
    updateItem(bandId: string, checklistId: string, itemId: string, dto: any): Promise<{
        id: string;
        sortOrder: number;
        category: string;
        checklistId: string;
        text: string;
        done: boolean;
    }>;
    removeItem(bandId: string, checklistId: string, itemId: string): Promise<void>;
    resetItems(bandId: string, checklistId: string): Promise<void>;
    removeContactById(bandId: string, contactId: string): Promise<void>;
    removeChecklistById(bandId: string, checklistId: string): Promise<void>;
    removeItemById(bandId: string, itemId: string): Promise<void>;
}

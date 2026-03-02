import { VenuesService } from './venues.service';
export declare class VenuesController {
    private readonly venues;
    constructor(venues: VenuesService);
    findAll(user: any): Promise<{
        createdAt: string;
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        city: string | null;
        address: string | null;
        website: string | null;
        capacity: number | null;
        bookingName: string | null;
        bookingEmail: string | null;
        bookingPhone: string | null;
    }[]>;
    create(user: any, dto: any): Promise<{
        createdAt: string;
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        city: string | null;
        address: string | null;
        website: string | null;
        capacity: number | null;
        bookingName: string | null;
        bookingEmail: string | null;
        bookingPhone: string | null;
    }>;
    update(user: any, id: string, dto: any): Promise<{
        createdAt: string;
        id: string;
        bandId: string;
        name: string;
        notes: string | null;
        city: string | null;
        address: string | null;
        website: string | null;
        capacity: number | null;
        bookingName: string | null;
        bookingEmail: string | null;
        bookingPhone: string | null;
    }>;
    remove(user: any, id: string): Promise<void>;
}

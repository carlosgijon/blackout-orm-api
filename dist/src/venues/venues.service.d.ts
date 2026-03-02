import { PrismaService } from '../prisma/prisma.service';
export declare class VenuesService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
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
    create(bandId: string, dto: any): Promise<{
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
    update(bandId: string, id: string, dto: any): Promise<{
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
    remove(bandId: string, id: string): Promise<void>;
}

import { PrismaService } from '../prisma/prisma.service';
export declare class CalendarService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
        id: any;
        type: any;
        title: any;
        date: any;
        endDate: any;
        memberId: any;
        memberName: any;
        allDay: any;
        notes: any;
        createdAt: any;
    }[]>;
    getByMonth(bandId: string, year: number, month: number): Promise<{
        id: any;
        type: any;
        title: any;
        date: any;
        endDate: any;
        memberId: any;
        memberName: any;
        allDay: any;
        notes: any;
        createdAt: any;
    }[]>;
    create(bandId: string, dto: any): Promise<{
        id: any;
        type: any;
        title: any;
        date: any;
        endDate: any;
        memberId: any;
        memberName: any;
        allDay: any;
        notes: any;
        createdAt: any;
    }>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: any;
        type: any;
        title: any;
        date: any;
        endDate: any;
        memberId: any;
        memberName: any;
        allDay: any;
        notes: any;
        createdAt: any;
    }>;
    remove(bandId: string, id: string): Promise<void>;
}

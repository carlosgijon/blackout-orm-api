import { CalendarService } from './calendar.service';
export declare class CalendarController {
    private readonly calendar;
    constructor(calendar: CalendarService);
    findAll(user: any, year?: string, month?: string): Promise<{
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
    create(user: any, dto: any): Promise<{
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
    update(user: any, id: string, dto: any): Promise<{
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
    remove(user: any, id: string): Promise<void>;
}

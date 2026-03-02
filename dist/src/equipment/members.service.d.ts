import { PrismaService } from '../prisma/prisma.service';
export declare class MembersService {
    #private;
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<{
        id: any;
        name: any;
        roles: any;
        stagePosition: any;
        vocalMicId: any;
        notes: any;
        sortOrder: any;
    }[]>;
    create(bandId: string, dto: any): Promise<{
        id: any;
        name: any;
        roles: any;
        stagePosition: any;
        vocalMicId: any;
        notes: any;
        sortOrder: any;
    }>;
    update(bandId: string, id: string, dto: any): Promise<{
        id: any;
        name: any;
        roles: any;
        stagePosition: any;
        vocalMicId: any;
        notes: any;
        sortOrder: any;
    }>;
    remove(bandId: string, id: string): Promise<void>;
}

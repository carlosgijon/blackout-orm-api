import { MembersService } from './members.service';
export declare class MembersController {
    private readonly members;
    constructor(members: MembersService);
    findAll(user: any): Promise<{
        id: any;
        name: any;
        roles: any;
        stagePosition: any;
        vocalMicId: any;
        notes: any;
        sortOrder: any;
    }[]>;
    create(user: any, dto: any): Promise<{
        id: any;
        name: any;
        roles: any;
        stagePosition: any;
        vocalMicId: any;
        notes: any;
        sortOrder: any;
    }>;
    update(user: any, id: string, dto: any): Promise<{
        id: any;
        name: any;
        roles: any;
        stagePosition: any;
        vocalMicId: any;
        notes: any;
        sortOrder: any;
    }>;
    remove(user: any, id: string): Promise<void>;
}

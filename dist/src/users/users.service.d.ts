import { PrismaService } from '../prisma/prisma.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
export declare class UsersService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(bandId: string): Promise<any[]>;
    create(bandId: string, dto: CreateUserDto): Promise<any>;
    update(bandId: string, id: string, dto: UpdateUserDto): Promise<any>;
    changePassword(bandId: string, id: string, newPassword: string): Promise<void>;
    remove(bandId: string, id: string, requesterId: string): Promise<void>;
    private sanitize;
}

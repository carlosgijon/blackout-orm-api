import { PrismaService } from '../prisma/prisma.service';
import { CreateBandDto } from './dto/create-band.dto';
export declare class BandsService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    findAll(): Promise<{
        id: string;
        createdAt: Date;
        name: string;
        slug: string;
        _count: {
            userBands: number;
        };
    }[]>;
    create(dto: CreateBandDto): Promise<{
        adminUser: {
            id: string;
            username: string;
        };
        id: string;
        createdAt: Date;
        name: string;
        slug: string;
        logo: string | null;
    }>;
    remove(id: string): Promise<void>;
    getMyBand(bandId: string): Promise<{
        id: string;
        createdAt: Date;
        name: string;
        slug: string;
        logo: string | null;
    }>;
    updateMyBand(bandId: string, dto: {
        name?: string;
        logo?: string | null;
    }): Promise<{
        id: string;
        createdAt: Date;
        name: string;
        slug: string;
        logo: string | null;
    }>;
}

import { BandsService } from './bands.service';
import { CreateBandDto } from './dto/create-band.dto';
export declare class BandsController {
    private readonly bands;
    constructor(bands: BandsService);
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
    getMyBand(user: any): Promise<{
        id: string;
        createdAt: Date;
        name: string;
        slug: string;
        logo: string | null;
    }>;
    updateMyBand(user: any, dto: {
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

import { JwtService } from '@nestjs/jwt';
import { PrismaService } from '../prisma/prisma.service';
import { LoginDto } from './dto/login.dto';
export declare class AuthService {
    #private;
    private readonly prisma;
    private readonly jwt;
    constructor(prisma: PrismaService, jwt: JwtService);
    login(dto: LoginDto): Promise<{
        token: string;
        user: any;
        bands: {
            id: string;
            name: string;
            slug: string;
            logo: string | undefined;
            role: string;
        }[];
    }>;
    selectBand(userId: string, bandId: string): Promise<{
        token: string;
        user: any;
        band: {
            id: string;
            name: string;
            slug: string;
            logo: string | undefined;
            role: string;
        };
    }>;
    getMe(userId: string, bandId: string | null): Promise<{
        user: any;
        band: {
            logo: string | undefined;
            role: string;
            id: string;
            name: string;
            slug: string;
        } | null;
    }>;
}

import { AuthService } from './auth.service';
import { LoginDto } from './dto/login.dto';
export declare class AuthController {
    private readonly auth;
    constructor(auth: AuthService);
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
    selectBand(user: any, bandId: string): Promise<{
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
    me(user: any): Promise<{
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

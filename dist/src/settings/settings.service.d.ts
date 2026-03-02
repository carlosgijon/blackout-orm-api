import { PrismaService } from '../prisma/prisma.service';
export declare class SettingsService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    get(bandId: string): Promise<{
        theme: string;
        bpmApiKey?: string;
    }>;
    set(bandId: string, partial: {
        theme?: string;
        bpmApiKey?: string;
    }): Promise<void>;
}

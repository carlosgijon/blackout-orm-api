import { PrismaService } from '../prisma/prisma.service';
export declare class ChannelListService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    generate(bandId: string): Promise<any[]>;
}

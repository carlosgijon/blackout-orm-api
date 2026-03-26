import { Injectable } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class SettingsService {
  constructor(private readonly prisma: PrismaService) {}

  async get(bandId: string): Promise<{ theme: string; bpmApiKey?: string; fontSize?: string; fontFamily?: string }> {
    const rows = await this.prisma.setting.findMany({ where: { bandId } });
    const map: Record<string, string> = {};
    for (const r of rows) map[r.key] = r.value;
    return {
      theme: map['theme'] ?? 'dark',
      bpmApiKey: map['bpmApiKey'],
      fontSize: map['fontSize'],
      fontFamily: map['fontFamily'],
    };
  }

  async set(bandId: string, partial: { theme?: string; bpmApiKey?: string; fontSize?: string; fontFamily?: string }): Promise<void> {
    const entries = Object.entries(partial).filter(([, v]) => v !== undefined) as [string, string][];
    for (const [key, value] of entries) {
      await this.prisma.setting.upsert({
        where: { bandId_key: { bandId, key } },
        update: { value },
        create: { bandId, key, value },
      });
    }
  }
}

import { Controller, Post, Body, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { AiService } from './ai.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';
import { PrismaService } from '../prisma/prisma.service';

@ApiTags('ai')
@ApiBearerAuth('JWT')
@Controller('ai')
@UseGuards(JwtAuthGuard, BandGuard)
export class AiController {
  constructor(
    private readonly ai: AiService,
    private readonly prisma: PrismaService,
  ) {}

  @Post('setlist')
  async generateSetlist(
    @CurrentUser() user: any,
    @Body() dto: { songs: any[]; preferences?: string },
  ) {
    // Get the Groq API key from band settings
    const setting = await this.prisma.setting.findUnique({
      where: { bandId_key: { bandId: user.bandId, key: 'groqApiKey' } },
    });
    const groqApiKey = setting?.value ?? '';
    return this.ai.generateSetlist(dto.songs, dto.preferences ?? '', groqApiKey);
  }
}

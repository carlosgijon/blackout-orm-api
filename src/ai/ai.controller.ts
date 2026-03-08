import { Controller, Post, Body, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { AiService } from './ai.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';

@ApiTags('ai')
@ApiBearerAuth('JWT')
@Controller('ai')
@UseGuards(JwtAuthGuard, BandGuard)
export class AiController {
  constructor(private readonly ai: AiService) {}

  @Post('setlist')
  generateSetlist(@Body() dto: { songs: any[]; preferences?: string }) {
    return this.ai.generateSetlist(dto.songs, dto.preferences ?? '');
  }
}

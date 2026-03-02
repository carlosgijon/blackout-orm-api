import { Controller, Get, Post, Body, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { SettingsService } from './settings.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('settings')
@ApiBearerAuth('JWT')
@Controller('settings')
@UseGuards(JwtAuthGuard, BandGuard)
export class SettingsController {
  constructor(private readonly settings: SettingsService) {}

  @Get()
  get(@CurrentUser() user: any) {
    return this.settings.get(user.bandId);
  }

  @Post()
  set(@CurrentUser() user: any, @Body() dto: { theme?: string; bpmApiKey?: string }) {
    return this.settings.set(user.bandId, dto);
  }
}

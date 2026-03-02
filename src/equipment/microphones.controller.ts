import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { MicrophonesService } from './microphones.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('microphones')
@ApiBearerAuth('JWT')
@Controller('microphones')
@UseGuards(JwtAuthGuard, BandGuard)
export class MicrophonesController {
  constructor(private readonly mics: MicrophonesService) {}

  @Get()
  findAll(@CurrentUser() user: any) { return this.mics.findAll(user.bandId); }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.mics.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.mics.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.mics.remove(user.bandId, id); }
}

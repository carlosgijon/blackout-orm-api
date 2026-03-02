import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { InstrumentsService } from './instruments.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('instruments')
@ApiBearerAuth('JWT')
@Controller('instruments')
@UseGuards(JwtAuthGuard, BandGuard)
export class InstrumentsController {
  constructor(private readonly instruments: InstrumentsService) {}

  @Get()
  findAll(@CurrentUser() user: any) { return this.instruments.findAll(user.bandId); }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.instruments.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.instruments.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.instruments.remove(user.bandId, id); }

  @Post(':id/mics')
  setMics(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: { micIds: string[] }) {
    return this.instruments.setMics(user.bandId, id, dto.micIds);
  }
}

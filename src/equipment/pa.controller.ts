import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { PaService } from './pa.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('pa-equipment')
@ApiBearerAuth('JWT')
@Controller('pa')
@UseGuards(JwtAuthGuard, BandGuard)
export class PaController {
  constructor(private readonly pa: PaService) {}

  @Get()
  findAll(@CurrentUser() user: any) { return this.pa.findAll(user.bandId); }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.pa.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.pa.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.pa.remove(user.bandId, id); }
}

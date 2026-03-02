import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { VenuesService } from './venues.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('venues')
@ApiBearerAuth('JWT')
@Controller('venues')
@UseGuards(JwtAuthGuard, BandGuard)
export class VenuesController {
  constructor(private readonly venues: VenuesService) {}

  @Get()
  findAll(@CurrentUser() user: any) { return this.venues.findAll(user.bandId); }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.venues.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.venues.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.venues.remove(user.bandId, id); }
}

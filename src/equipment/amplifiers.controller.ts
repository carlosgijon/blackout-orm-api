import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { AmplifiersService } from './amplifiers.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('amplifiers')
@ApiBearerAuth('JWT')
@Controller('amplifiers')
@UseGuards(JwtAuthGuard, BandGuard)
export class AmplifiersController {
  constructor(private readonly amplifiers: AmplifiersService) {}

  @Get()
  findAll(@CurrentUser() user: any) { return this.amplifiers.findAll(user.bandId); }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.amplifiers.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.amplifiers.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.amplifiers.remove(user.bandId, id); }

  @Put(':id/instrument-link')
  updateInstrumentLink(
    @CurrentUser() user: any,
    @Param('id') id: string,
    @Body() dto: { instrumentId: string | null },
  ) {
    return this.amplifiers.updateInstrumentLink(user.bandId, id, dto.instrumentId);
  }

  @Post(':id/mics')
  setMics(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: { micIds: string[] }) {
    return this.amplifiers.setMics(user.bandId, id, dto.micIds);
  }
}

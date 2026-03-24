import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { RehearsalsService } from './rehearsals.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('rehearsals')
@ApiBearerAuth('JWT')
@Controller('rehearsals')
@UseGuards(JwtAuthGuard, BandGuard)
export class RehearsalsController {
  constructor(private readonly svc: RehearsalsService) {}

  @Get()
  findAll(@CurrentUser() user: any) {
    return this.svc.findAll(user.bandId);
  }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) {
    return this.svc.create(user.bandId, dto);
  }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.svc.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) {
    return this.svc.remove(user.bandId, id);
  }

  @Post(':id/songs')
  addSong(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.svc.addSong(user.bandId, id, dto);
  }

  @Put(':id/songs/:entryId')
  updateSong(@CurrentUser() user: any, @Param('id') id: string, @Param('entryId') entryId: string, @Body() dto: any) {
    return this.svc.updateSong(user.bandId, id, entryId, dto);
  }

  @Delete(':id/songs/:entryId')
  removeSong(@CurrentUser() user: any, @Param('id') id: string, @Param('entryId') entryId: string) {
    return this.svc.removeSong(user.bandId, id, entryId);
  }
}

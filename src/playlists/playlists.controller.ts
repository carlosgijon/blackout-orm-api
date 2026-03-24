import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { PlaylistsService } from './playlists.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('playlists')
@ApiBearerAuth('JWT')
@Controller('playlists')
@UseGuards(JwtAuthGuard, BandGuard)
export class PlaylistsController {
  constructor(private readonly playlists: PlaylistsService) {}

  @Get()
  findAll(@CurrentUser() user: any) {
    return this.playlists.findAll(user.bandId);
  }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) {
    return this.playlists.create(user.bandId, dto);
  }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.playlists.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) {
    return this.playlists.remove(user.bandId, id);
  }

  @Get(':id/songs')
  getSongs(@CurrentUser() user: any, @Param('id') id: string) {
    return this.playlists.getSongs(user.bandId, id);
  }

  @Post(':id/songs')
  addSong(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.playlists.addSong(user.bandId, id, dto);
  }

  @Put(':id/songs/:entryId')
  updateSong(
    @CurrentUser() user: any,
    @Param('id') id: string,
    @Param('entryId') entryId: string,
    @Body() dto: any,
  ) {
    return this.playlists.updateSong(user.bandId, id, entryId, dto);
  }

  @Delete(':id/songs/:entryId')
  removeSong(@CurrentUser() user: any, @Param('id') id: string, @Param('entryId') entryId: string) {
    return this.playlists.removeSong(user.bandId, id, entryId);
  }

  @Post(':id/reorder')
  reorder(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: { ids: string[] }) {
    return this.playlists.reorder(user.bandId, id, dto.ids);
  }

  @Get(':id/gigs')
  getGigs(@CurrentUser() user: any, @Param('id') id: string) {
    return this.playlists.getGigs(user.bandId, id);
  }
}

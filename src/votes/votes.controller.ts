import {
  Controller, Get, Post, Put, Delete,
  Param, Body, Query, UseGuards, Request,
} from '@nestjs/common';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { VotesService } from './votes.service';

@Controller('vote-sessions')
@UseGuards(JwtAuthGuard)
export class VotesController {
  constructor(private readonly votesService: VotesService) {}

  @Get()
  getSession(@Request() req: any, @Query('playlistId') playlistId: string) {
    return this.votesService.getSession(req.user.bandId, playlistId);
  }

  @Post()
  createSession(
    @Request() req: any,
    @Body() body: { playlistId: string; title: string },
  ) {
    return this.votesService.createSession(req.user.bandId, body.playlistId, body.title);
  }

  @Put(':id/close')
  closeSession(@Request() req: any, @Param('id') id: string) {
    return this.votesService.closeSession(req.user.bandId, id);
  }

  @Put(':id/reopen')
  reopenSession(@Request() req: any, @Param('id') id: string) {
    return this.votesService.reopenSession(req.user.bandId, id);
  }

  @Delete(':id')
  deleteSession(@Request() req: any, @Param('id') id: string) {
    return this.votesService.deleteSession(req.user.bandId, id);
  }

  @Post(':id/votes')
  castVote(
    @Request() req: any,
    @Param('id') id: string,
    @Body() body: { voterName: string; orderedIds: string[] },
  ) {
    return this.votesService.castVote(req.user.bandId, id, body.voterName, body.orderedIds);
  }

  @Get(':id/results')
  getResults(@Request() req: any, @Param('id') id: string) {
    return this.votesService.getResults(req.user.bandId, id);
  }
}

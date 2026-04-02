import {
  Controller, Get, Post, Patch, Delete,
  Param, Body, UseGuards, Request,
} from '@nestjs/common';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { PollsService, PollStatus } from './polls.service';

@Controller('polls')
@UseGuards(JwtAuthGuard)
export class PollsController {
  constructor(private readonly polls: PollsService) {}

  // ── Poll CRUD ──────────────────────────────────────────────────────────────

  @Get()
  list(@Request() req: any) {
    return this.polls.list(req.user.bandId);
  }

  @Get(':id')
  get(@Request() req: any, @Param('id') id: string) {
    return this.polls.get(req.user.bandId, id);
  }

  @Post()
  create(@Request() req: any, @Body() body: any) {
    return this.polls.create(req.user.bandId, body);
  }

  @Patch(':id/status')
  setStatus(@Request() req: any, @Param('id') id: string, @Body() body: { status: PollStatus }) {
    return this.polls.setStatus(req.user.bandId, id, body.status);
  }

  @Delete(':id')
  delete(@Request() req: any, @Param('id') id: string) {
    return this.polls.delete(req.user.bandId, id);
  }

  // ── Options ────────────────────────────────────────────────────────────────

  @Post(':id/options')
  addOption(
    @Request() req: any,
    @Param('id') id: string,
    @Body() body: { text: string; proposedBy: string },
  ) {
    return this.polls.addOption(req.user.bandId, id, body.text, body.proposedBy);
  }

  @Delete(':id/options/:optionId')
  deleteOption(
    @Request() req: any,
    @Param('id') id: string,
    @Param('optionId') optionId: string,
  ) {
    return this.polls.deleteOption(req.user.bandId, id, optionId);
  }

  // ── Votes ──────────────────────────────────────────────────────────────────

  @Post(':id/votes')
  castVote(
    @Request() req: any,
    @Param('id') id: string,
    @Body() body: { voterName: string; value?: string; approvedOptionIds?: string[]; comment?: string },
  ) {
    return this.polls.castVote(req.user.bandId, id, body);
  }

  // ── Results ────────────────────────────────────────────────────────────────

  @Get(':id/results')
  getResults(@Request() req: any, @Param('id') id: string) {
    return this.polls.getResults(req.user.bandId, id);
  }
}

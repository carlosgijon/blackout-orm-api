import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { MerchService } from './merch.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('merch')
@ApiBearerAuth('JWT')
@Controller('merch')
@UseGuards(JwtAuthGuard, BandGuard)
export class MerchController {
  constructor(private readonly merch: MerchService) {}

  @Get()
  findAll(@CurrentUser() user: any) {
    return this.merch.findAll(user.bandId);
  }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) {
    return this.merch.create(user.bandId, dto);
  }

  // ── Waiting list — literal routes BEFORE :id to avoid conflicts ──

  @Get('waiting')
  getAllWaiting(@CurrentUser() user: any) {
    return this.merch.getAllWaiting(user.bandId);
  }

  @Put('waiting/:entryId')
  updateWaitingEntry(@CurrentUser() user: any, @Param('entryId') entryId: string, @Body() dto: any) {
    return this.merch.updateWaitingEntry(user.bandId, entryId, dto);
  }

  @Delete('waiting/:entryId')
  removeWaitingEntry(@CurrentUser() user: any, @Param('entryId') entryId: string) {
    return this.merch.removeWaitingEntry(user.bandId, entryId);
  }

  // ── Item routes ──────────────────────────────────────────────────

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.merch.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) {
    return this.merch.remove(user.bandId, id);
  }

  @Post(':id/sell')
  sell(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.merch.sell(user.bandId, id, dto);
  }

  @Put(':id/stock')
  restock(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.merch.restock(user.bandId, id, dto);
  }

  @Post(':id/waiting')
  addToWaitingList(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.merch.addToWaitingList(user.bandId, id, dto);
  }
}

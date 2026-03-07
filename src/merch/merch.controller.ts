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
}

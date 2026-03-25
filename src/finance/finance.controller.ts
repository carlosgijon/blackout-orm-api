import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { FinanceService } from './finance.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('finance')
@ApiBearerAuth('JWT')
@Controller('finance')
@UseGuards(JwtAuthGuard, BandGuard)
export class FinanceController {
  constructor(private readonly finance: FinanceService) {}

  // -- Transactions --

  @Get('transactions')
  findAllTransactions(@CurrentUser() user: any) {
    return this.finance.findAllTransactions(user.bandId);
  }

  @Post('transactions')
  createTransaction(@CurrentUser() user: any, @Body() dto: any) {
    return this.finance.createTransaction(user.bandId, dto);
  }

  @Put('transactions/:id')
  updateTransaction(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.finance.updateTransaction(user.bandId, id, dto);
  }

  @Delete('transactions/:id')
  removeTransaction(@CurrentUser() user: any, @Param('id') id: string) {
    return this.finance.removeTransaction(user.bandId, id);
  }

  // -- Wish List --

  @Get('wish-list')
  findAllWishList(@CurrentUser() user: any) {
    return this.finance.findAllWishList(user.bandId);
  }

  @Post('wish-list')
  createWishListItem(@CurrentUser() user: any, @Body() dto: any) {
    return this.finance.createWishListItem(user.bandId, dto);
  }

  @Put('wish-list/:id')
  updateWishListItem(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.finance.updateWishListItem(user.bandId, id, dto);
  }

  @Delete('wish-list/:id')
  removeWishListItem(@CurrentUser() user: any, @Param('id') id: string) {
    return this.finance.removeWishListItem(user.bandId, id);
  }

  // -- Initial Balance --

  @Get('balance')
  getInitialBalance(@CurrentUser() user: any) {
    return this.finance.getInitialBalance(user.bandId);
  }

  @Put('balance')
  setInitialBalance(@CurrentUser() user: any, @Body() body: { initialBalance: number }) {
    return this.finance.setInitialBalance(user.bandId, body.initialBalance);
  }
}

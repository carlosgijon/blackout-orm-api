import { Controller, Get, Post, Put, Delete, Body, Param, Query, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { CalendarService } from './calendar.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('calendar')
@ApiBearerAuth('JWT')
@Controller('calendar')
@UseGuards(JwtAuthGuard, BandGuard)
export class CalendarController {
  constructor(private readonly calendar: CalendarService) {}

  @Get()
  findAll(
    @CurrentUser() user: any,
    @Query('year') year?: string,
    @Query('month') month?: string,
  ) {
    if (year && month) {
      return this.calendar.getByMonth(user.bandId, Number(year), Number(month));
    }
    return this.calendar.findAll(user.bandId);
  }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.calendar.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.calendar.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.calendar.remove(user.bandId, id); }
}

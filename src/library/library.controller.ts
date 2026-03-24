import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { LibraryService } from './library.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('library')
@ApiBearerAuth('JWT')
@Controller('library')
@UseGuards(JwtAuthGuard, BandGuard)
export class LibraryController {
  constructor(private readonly library: LibraryService) {}

  @Get()
  findAll(@CurrentUser() user: any) {
    return this.library.findAll(user.bandId);
  }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) {
    return this.library.create(user.bandId, dto);
  }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.library.update(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) {
    return this.library.remove(user.bandId, id);
  }

  @Get('stats')
  getStats(@CurrentUser() user: any) {
    return this.library.getStats(user.bandId);
  }

  @Get(':id/usage')
  getUsage(@CurrentUser() user: any, @Param('id') id: string) {
    return this.library.getUsage(user.bandId, id);
  }
}

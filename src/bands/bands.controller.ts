import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { BandsService } from './bands.service';
import { CreateBandDto } from './dto/create-band.dto';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { SuperAdminGuard, AdminGuard } from '../auth/guards/admin.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('bands')
@ApiBearerAuth('JWT')
@Controller('bands')
export class BandsController {
  constructor(private readonly bands: BandsService) {}

  // -- Superadmin: manage all bands --

  @Get()
  @UseGuards(JwtAuthGuard, SuperAdminGuard)
  findAll() {
    return this.bands.findAll();
  }

  @Post()
  @UseGuards(JwtAuthGuard, SuperAdminGuard)
  create(@Body() dto: CreateBandDto) {
    return this.bands.create(dto);
  }

  @Delete(':id')
  @UseGuards(JwtAuthGuard, SuperAdminGuard)
  remove(@Param('id') id: string) {
    return this.bands.remove(id);
  }

  // -- Admin: manage own band --

  @Get('mine')
  @UseGuards(JwtAuthGuard, BandGuard)
  @ApiOperation({ summary: 'Obtener información de la banda actual' })
  getMyBand(@CurrentUser() user: any) {
    return this.bands.getMyBand(user.bandId);
  }

  @Put('mine')
  @UseGuards(JwtAuthGuard, BandGuard, AdminGuard)
  @ApiOperation({ summary: 'Actualizar nombre y logo de la banda' })
  updateMyBand(@CurrentUser() user: any, @Body() dto: { name?: string; logo?: string | null }) {
    return this.bands.updateMyBand(user.bandId, dto);
  }
}

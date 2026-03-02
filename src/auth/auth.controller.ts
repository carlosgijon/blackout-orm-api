import { Controller, Post, Get, Body, UseGuards, Param } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { AuthService } from './auth.service';
import { LoginDto } from './dto/login.dto';
import { JwtAuthGuard } from './guards/jwt.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('auth')
@Controller('auth')
export class AuthController {
  constructor(private readonly auth: AuthService) {}

  @Post('login')
  @ApiOperation({ summary: 'Iniciar sesión' })
  login(@Body() dto: LoginDto) {
    return this.auth.login(dto);
  }

  @Post('select-band/:bandId')
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth('JWT')
  @ApiOperation({ summary: 'Seleccionar banda activa y obtener token con bandId' })
  selectBand(@CurrentUser() user: any, @Param('bandId') bandId: string) {
    return this.auth.selectBand(user.id, bandId);
  }

  @Get('me')
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth('JWT')
  @ApiOperation({ summary: 'Usuario actual + banda activa' })
  me(@CurrentUser() user: any) {
    return this.auth.getMe(user.id, user.bandId ?? null);
  }
}

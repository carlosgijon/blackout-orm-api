import {
  Controller, Get, Post, Put, Delete, Body, Param, UseGuards,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { ChangePasswordDto } from './dto/change-password.dto';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { AdminGuard } from '../auth/guards/admin.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('users')
@ApiBearerAuth('JWT')
@Controller('users')
@UseGuards(JwtAuthGuard, AdminGuard)
export class UsersController {
  constructor(private readonly users: UsersService) {}

  @Get()
  findAll(@CurrentUser() user: any) {
    return this.users.findAll(user.bandId);
  }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: CreateUserDto) {
    return this.users.create(user.bandId, dto);
  }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: UpdateUserDto) {
    return this.users.update(user.bandId, id, dto);
  }

  @Put(':id/password')
  changePassword(
    @CurrentUser() user: any,
    @Param('id') id: string,
    @Body() dto: ChangePasswordDto,
  ) {
    return this.users.changePassword(user.bandId, id, dto.newPassword);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) {
    return this.users.remove(user.bandId, id, user.id);
  }
}

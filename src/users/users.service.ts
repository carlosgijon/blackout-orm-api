import {
  Injectable,
  NotFoundException,
  BadRequestException,
  ForbiddenException,
} from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import * as bcrypt from 'bcrypt';

@Injectable()
export class UsersService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll(bandId: string) {
    const users = await this.prisma.user.findMany({
      where: { bandId },
      include: { band: true },
      orderBy: { createdAt: 'asc' },
    });
    return users.map(this.sanitize);
  }

  async create(bandId: string, dto: CreateUserDto) {
    const exists = await this.prisma.user.findUnique({ where: { username: dto.username } });
    if (exists) throw new BadRequestException(`El usuario "${dto.username}" ya existe`);

    const passwordHash = await bcrypt.hash(dto.password, 10);
    const user = await this.prisma.user.create({
      data: {
        username: dto.username,
        displayName: dto.displayName,
        passwordHash,
        role: dto.role,
        bandId,
        userBands: { create: { bandId, role: dto.role } },
      },
      include: { band: true },
    });
    return this.sanitize(user);
  }

  async update(bandId: string, id: string, dto: UpdateUserDto) {
    const user = await this.prisma.user.findFirst({ where: { id, bandId } });
    if (!user) throw new NotFoundException('Usuario no encontrado');

    if (dto.username && dto.username !== user.username) {
      const exists = await this.prisma.user.findUnique({ where: { username: dto.username } });
      if (exists) throw new BadRequestException(`El usuario "${dto.username}" ya existe`);
    }

    const updated = await this.prisma.user.update({
      where: { id },
      data: dto,
      include: { band: true },
    });
    return this.sanitize(updated);
  }

  async changePassword(bandId: string, id: string, newPassword: string) {
    const user = await this.prisma.user.findFirst({ where: { id, bandId } });
    if (!user) throw new NotFoundException('Usuario no encontrado');

    const passwordHash = await bcrypt.hash(newPassword, 10);
    await this.prisma.user.update({ where: { id }, data: { passwordHash } });
  }

  async remove(bandId: string, id: string, requesterId: string) {
    if (id === requesterId) throw new ForbiddenException('No puedes eliminar tu propio usuario');
    const user = await this.prisma.user.findFirst({ where: { id, bandId } });
    if (!user) throw new NotFoundException('Usuario no encontrado');
    await this.prisma.user.delete({ where: { id } });
  }

  private sanitize(user: any) {
    const { passwordHash: _ph, ...rest } = user;
    return rest;
  }
}

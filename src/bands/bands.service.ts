import { Injectable, BadRequestException, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { CreateBandDto } from './dto/create-band.dto';
import * as bcrypt from 'bcrypt';

@Injectable()
export class BandsService {
  constructor(private readonly prisma: PrismaService) {}

  async findAll() {
    return this.prisma.band.findMany({
      select: { id: true, name: true, slug: true, createdAt: true, _count: { select: { userBands: true } } },
      orderBy: { createdAt: 'asc' },
    });
  }

  async create(dto: CreateBandDto) {
    const exists = await this.prisma.band.findUnique({ where: { slug: dto.slug } });
    if (exists) throw new BadRequestException(`El slug "${dto.slug}" ya está en uso`);

    const userExists = await this.prisma.user.findUnique({ where: { username: dto.adminUsername } });
    if (userExists) throw new BadRequestException(`El usuario "${dto.adminUsername}" ya existe`);

    const passwordHash = await bcrypt.hash(dto.adminPassword, 10);
    const band = await this.prisma.band.create({ data: { name: dto.name, slug: dto.slug } });

    const user = await this.prisma.user.create({
      data: {
        username: dto.adminUsername,
        passwordHash,
        role: 'admin',
        bandId: band.id,
        userBands: { create: { bandId: band.id, role: 'admin' } },
      },
    });

    return { ...band, adminUser: { id: user.id, username: user.username } };
  }

  async remove(id: string) {
    const band = await this.prisma.band.findUnique({ where: { id } });
    if (!band) throw new NotFoundException('Banda no encontrada');
    await this.prisma.band.delete({ where: { id } });
  }

  async getMyBand(bandId: string) {
    const band = await this.prisma.band.findUnique({ where: { id: bandId } });
    if (!band) throw new NotFoundException('Banda no encontrada');
    return band;
  }

  async updateMyBand(bandId: string, dto: { name?: string; logo?: string | null }) {
    await this.getMyBand(bandId);
    return this.prisma.band.update({ where: { id: bandId }, data: dto });
  }
}

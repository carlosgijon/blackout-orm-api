import { Injectable, UnauthorizedException, NotFoundException, ForbiddenException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { PrismaService } from '../prisma/prisma.service';
import { LoginDto } from './dto/login.dto';
import * as bcrypt from 'bcrypt';
import { JwtPayload } from './strategies/jwt.strategy';

@Injectable()
export class AuthService {
  constructor(
    private readonly prisma: PrismaService,
    private readonly jwt: JwtService,
  ) {}

  async login(dto: LoginDto) {
    const user = await this.prisma.user.findUnique({ where: { username: dto.username } });
    if (!user || !user.isActive) throw new UnauthorizedException('Credenciales incorrectas');

    const valid = await bcrypt.compare(dto.password, user.passwordHash);
    if (!valid) throw new UnauthorizedException('Credenciales incorrectas');

    // Get bands this user belongs to via UserBand
    const memberships = await this.prisma.userBand.findMany({
      where: { userId: user.id },
      include: { band: { select: { id: true, name: true, slug: true, logo: true } } },
    });

    const bands = memberships.map(m => ({
      id: m.band.id,
      name: m.band.name,
      slug: m.band.slug,
      logo: m.band.logo ?? undefined,
      role: m.role,
    }));

    // Embed bandId in token when there is exactly one band (or zero for superadmin)
    const bandIdForToken = bands.length === 1 ? bands[0].id : null;

    const payload: JwtPayload = {
      sub: user.id,
      username: user.username,
      role: user.role,
      bandId: bandIdForToken,
    };

    return {
      token: this.jwt.sign(payload),
      user: this.#sanitize(user),
      bands,
    };
  }

  async selectBand(userId: string, bandId: string) {
    const membership = await this.prisma.userBand.findUnique({
      where: { userId_bandId: { userId, bandId } },
      include: { band: { select: { id: true, name: true, slug: true, logo: true } } },
    });
    if (!membership) throw new ForbiddenException('No perteneces a esta banda');

    const user = await this.prisma.user.findUnique({ where: { id: userId } });
    if (!user || !user.isActive) throw new UnauthorizedException('Usuario inactivo');

    const payload: JwtPayload = {
      sub: user.id,
      username: user.username,
      role: membership.role,
      bandId,
    };

    return {
      token: this.jwt.sign(payload),
      user: this.#sanitize(user),
      band: {
        id: membership.band.id,
        name: membership.band.name,
        slug: membership.band.slug,
        logo: membership.band.logo ?? undefined,
        role: membership.role,
      },
    };
  }

  async getMe(userId: string, bandId: string | null) {
    const user = await this.prisma.user.findUnique({ where: { id: userId } });
    if (!user) throw new NotFoundException('Usuario no encontrado');

    let band = null;
    if (bandId) {
      const b = await this.prisma.band.findUnique({
        where: { id: bandId },
        select: { id: true, name: true, slug: true, logo: true },
      });
      if (b) {
        const membership = await this.prisma.userBand.findUnique({
          where: { userId_bandId: { userId, bandId } },
        });
        band = { ...b, logo: b.logo ?? undefined, role: membership?.role ?? user.role };
      }
    }

    return { user: this.#sanitize(user), band };
  }

  #sanitize(user: any) {
    const { passwordHash: _ph, ...rest } = user;
    return rest;
  }
}

import { Injectable, CanActivate, ExecutionContext, ForbiddenException } from '@nestjs/common';

@Injectable()
export class AdminGuard implements CanActivate {
  canActivate(ctx: ExecutionContext): boolean {
    const user = ctx.switchToHttp().getRequest().user;
    if (!user || (user.role !== 'admin' && user.role !== 'superadmin')) {
      throw new ForbiddenException('Se requiere rol de administrador');
    }
    return true;
  }
}

@Injectable()
export class SuperAdminGuard implements CanActivate {
  canActivate(ctx: ExecutionContext): boolean {
    const user = ctx.switchToHttp().getRequest().user;
    if (!user || user.role !== 'superadmin') {
      throw new ForbiddenException('Se requiere rol de superadministrador');
    }
    return true;
  }
}

import { CanActivate, ExecutionContext, ForbiddenException, Injectable } from '@nestjs/common';

@Injectable()
export class BandGuard implements CanActivate {
  canActivate(context: ExecutionContext): boolean {
    const user = context.switchToHttp().getRequest().user;
    if (!user?.bandId) {
      throw new ForbiddenException('This endpoint requires a band membership');
    }
    return true;
  }
}

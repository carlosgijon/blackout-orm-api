import { CanActivate, ExecutionContext } from '@nestjs/common';
export declare class BandGuard implements CanActivate {
    canActivate(context: ExecutionContext): boolean;
}

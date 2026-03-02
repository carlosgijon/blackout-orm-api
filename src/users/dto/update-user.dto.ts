import { IsString, IsOptional, IsIn, IsBoolean } from 'class-validator';

export class UpdateUserDto {
  @IsOptional()
  @IsString()
  username?: string;

  @IsOptional()
  @IsString()
  displayName?: string;

  @IsOptional()
  @IsIn(['admin', 'member'])
  role?: 'admin' | 'member';

  @IsOptional()
  @IsBoolean()
  isActive?: boolean;
}

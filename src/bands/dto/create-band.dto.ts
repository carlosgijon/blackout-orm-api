import { IsString, MinLength, Matches } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class CreateBandDto {
  @ApiProperty({ example: 'Blackout' })
  @IsString()
  @MinLength(2)
  name: string;

  @ApiProperty({ example: 'blackout', description: 'Solo minúsculas, números y guiones' })
  @IsString()
  @Matches(/^[a-z0-9-]+$/, { message: 'El slug solo puede contener minúsculas, números y guiones' })
  slug: string;

  @ApiProperty({ example: 'admin_blackout', description: 'Username del administrador de la banda' })
  @IsString()
  adminUsername: string;

  @ApiProperty({ example: 'password123', minLength: 4 })
  @IsString()
  @MinLength(4)
  adminPassword: string;
}

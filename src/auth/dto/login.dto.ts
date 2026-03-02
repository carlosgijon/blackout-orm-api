import { IsString, MinLength } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class LoginDto {
  @ApiProperty({ example: 'superadmin' })
  @IsString()
  username: string;

  @ApiProperty({ example: 'superadmin123' })
  @IsString()
  @MinLength(4)
  password: string;
}

import { IsString, IsOptional, IsIn, MinLength } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateUserDto {
  @ApiProperty({ example: 'carlos' })
  @IsString()
  username: string;

  @ApiPropertyOptional({ example: 'Carlos García' })
  @IsOptional()
  @IsString()
  displayName?: string;

  @ApiProperty({ example: 'mipassword123', minLength: 4 })
  @IsString()
  @MinLength(4)
  password: string;

  @ApiProperty({ enum: ['admin', 'member'], example: 'member' })
  @IsIn(['admin', 'member'])
  role: 'admin' | 'member';
}

import { Module } from '@nestjs/common';
import { MerchService } from './merch.service';
import { MerchController } from './merch.controller';

@Module({
  controllers: [MerchController],
  providers: [MerchService],
})
export class MerchModule {}

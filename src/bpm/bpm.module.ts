import { Module } from '@nestjs/common';
import { BpmController } from './bpm.controller';
import { BpmService } from './bpm.service';

@Module({
  controllers: [BpmController],
  providers: [BpmService],
})
export class BpmModule {}

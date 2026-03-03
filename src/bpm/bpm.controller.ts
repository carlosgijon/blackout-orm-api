import { Controller, Get, Query } from '@nestjs/common';
import { BpmService } from './bpm.service';

@Controller('bpm-lookup')
export class BpmController {
  constructor(private readonly bpmService: BpmService) {}

  @Get()
  async lookup(
    @Query('title') title: string,
    @Query('artist') artist: string,
  ): Promise<{ bpm: number | null }> {
    const bpm = await this.bpmService.lookup(title ?? '', artist ?? '');
    return { bpm };
  }
}

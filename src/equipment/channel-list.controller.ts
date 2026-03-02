import { Controller, Get, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { ChannelListService } from './channel-list.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('channel-list')
@ApiBearerAuth('JWT')
@Controller('channel-list')
@UseGuards(JwtAuthGuard, BandGuard)
export class ChannelListController {
  constructor(private readonly channelList: ChannelListService) {}

  @Get()
  generate(@CurrentUser() user: any) {
    return this.channelList.generate(user.bandId);
  }
}

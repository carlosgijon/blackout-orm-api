import { Module } from '@nestjs/common';
import { MembersService } from './members.service';
import { MicrophonesService } from './microphones.service';
import { InstrumentsService } from './instruments.service';
import { AmplifiersService } from './amplifiers.service';
import { PaService } from './pa.service';
import { MembersController } from './members.controller';
import { MicrophonesController } from './microphones.controller';
import { InstrumentsController } from './instruments.controller';
import { AmplifiersController } from './amplifiers.controller';
import { PaController } from './pa.controller';
import { ChannelListController } from './channel-list.controller';
import { ChannelListService } from './channel-list.service';

@Module({
  controllers: [
    MembersController,
    MicrophonesController,
    InstrumentsController,
    AmplifiersController,
    PaController,
    ChannelListController,
  ],
  providers: [
    MembersService,
    MicrophonesService,
    InstrumentsService,
    AmplifiersService,
    PaService,
    ChannelListService,
  ],
})
export class EquipmentModule {}

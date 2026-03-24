import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { PrismaModule } from './prisma/prisma.module';
import { AuthModule } from './auth/auth.module';
import { UsersModule } from './users/users.module';
import { BandsModule } from './bands/bands.module';
import { SettingsModule } from './settings/settings.module';
import { LibraryModule } from './library/library.module';
import { PlaylistsModule } from './playlists/playlists.module';
import { EquipmentModule } from './equipment/equipment.module';
import { VenuesModule } from './venues/venues.module';
import { GigsModule } from './gigs/gigs.module';
import { CalendarModule } from './calendar/calendar.module';
import { BpmModule } from './bpm/bpm.module';
import { FinanceModule } from './finance/finance.module';
import { MerchModule } from './merch/merch.module';
import { AiModule } from './ai/ai.module';
import { RehearsalsModule } from './rehearsals/rehearsals.module';
import { VotesModule } from './votes/votes.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    PrismaModule,
    AuthModule,
    UsersModule,
    BandsModule,
    SettingsModule,
    LibraryModule,
    PlaylistsModule,
    EquipmentModule,
    VenuesModule,
    GigsModule,
    CalendarModule,
    BpmModule,
    FinanceModule,
    MerchModule,
    AiModule,
    RehearsalsModule,
    VotesModule,
  ],
})
export class AppModule {}

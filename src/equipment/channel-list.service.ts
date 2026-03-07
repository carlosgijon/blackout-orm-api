import { Injectable } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';

@Injectable()
export class ChannelListService {
  constructor(private readonly prisma: PrismaService) {}

  async generate(bandId: string) {
    const [members, instruments, mics] = await Promise.all([
      this.prisma.bandMember.findMany({ where: { bandId }, orderBy: { sortOrder: 'asc' } }),
      this.prisma.instrument.findMany({ where: { bandId }, orderBy: { channelOrder: 'asc' } }),
      this.prisma.microphone.findMany({ where: { bandId } }),
    ]);

    const memberMap = new Map(members.map(m => [m.id, m]));
    const micMap = new Map(mics.map(m => [m.id, m]));

    const channels: any[] = [];
    let ch = 1;

    // Vocal mics for members
    for (const member of members) {
      if (member.vocalMicId) {
        const mic = micMap.get(member.vocalMicId);
        channels.push({
          channelNumber: ch++,
          name: `${member.name} – Voz`,
          monoStereo: mic?.monoStereo ?? 'mono',
          phantomPower: mic?.phantomPower ?? false,
          micModel: mic ? `${mic.brand ?? ''} ${mic.model ?? ''}`.trim() || mic.name : undefined,
          micType: mic?.type,
          polarPattern: mic?.polarPattern ?? undefined,
          notes: undefined,
          memberId: member.id,
        });
      }
    }

    // Instruments
    const drumUsageLabel: Record<string, string> = {
      'drums-kick': 'Bombo',
      'drums-overhead': 'Aéreos',
      'drums-snare': 'Caja',
    };

    for (const inst of instruments) {
      const memberName = inst.memberId ? memberMap.get(inst.memberId)?.name : undefined;
      const instMics = mics.filter(m => m.assignedToId === inst.id && m.assignedToType === 'instrument');

      if (instMics.length === 0) {
        channels.push({
          channelNumber: ch++,
          name: memberName ? `${memberName} – ${inst.name}` : inst.name,
          monoStereo: inst.monoStereo ?? 'mono',
          phantomPower: false,
          notes: inst.notes ?? undefined,
          memberId: inst.memberId ?? undefined,
        });
      } else {
        for (const mic of instMics) {
          const suffix = instMics.length > 1
            ? (drumUsageLabel[mic.usage ?? ''] ?? inst.name)
            : inst.name;
          const name = memberName ? `${memberName} – ${suffix}` : suffix;
          channels.push({
            channelNumber: ch++,
            name,
            monoStereo: mic.monoStereo ?? 'mono',
            phantomPower: mic.phantomPower ?? false,
            micModel: `${mic.brand ?? ''} ${mic.model ?? ''}`.trim() || mic.name,
            micType: mic.type,
            polarPattern: mic.polarPattern ?? undefined,
            notes: inst.notes ?? undefined,
            memberId: inst.memberId ?? undefined,
          });
        }
      }
    }

    return channels;
  }
}

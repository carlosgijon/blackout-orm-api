"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChannelListService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let ChannelListService = class ChannelListService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async generate(bandId) {
        const [members, instruments, mics] = await Promise.all([
            this.prisma.bandMember.findMany({ where: { bandId }, orderBy: { sortOrder: 'asc' } }),
            this.prisma.instrument.findMany({ where: { bandId }, orderBy: { channelOrder: 'asc' } }),
            this.prisma.microphone.findMany({ where: { bandId } }),
        ]);
        const memberMap = new Map(members.map(m => [m.id, m]));
        const micMap = new Map(mics.map(m => [m.id, m]));
        const channels = [];
        let ch = 1;
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
        for (const inst of instruments) {
            const memberName = inst.memberId ? memberMap.get(inst.memberId)?.name : undefined;
            const assignedMic = mics.find(m => m.assignedToId === inst.id && m.assignedToType === 'instrument');
            channels.push({
                channelNumber: ch++,
                name: memberName ? `${memberName} – ${inst.name}` : inst.name,
                monoStereo: inst.monoStereo ?? 'mono',
                phantomPower: assignedMic?.phantomPower ?? false,
                micModel: assignedMic ? `${assignedMic.brand ?? ''} ${assignedMic.model ?? ''}`.trim() || assignedMic.name : undefined,
                micType: assignedMic?.type,
                polarPattern: assignedMic?.polarPattern ?? undefined,
                notes: inst.notes ?? undefined,
                memberId: inst.memberId ?? undefined,
            });
        }
        return channels;
    }
};
exports.ChannelListService = ChannelListService;
exports.ChannelListService = ChannelListService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], ChannelListService);
//# sourceMappingURL=channel-list.service.js.map
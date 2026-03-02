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
exports.AmplifiersService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let AmplifiersService = class AmplifiersService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    findAll(bandId) {
        return this.prisma.amplifier.findMany({ where: { bandId } });
    }
    create(bandId, dto) {
        const { id: _id, ...data } = dto;
        return this.prisma.amplifier.create({ data: { ...data, bandId } });
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const { id: _id, bandId: _bid, ...data } = dto;
        return this.prisma.amplifier.update({ where: { id }, data });
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.amplifier.delete({ where: { id } });
    }
    async updateInstrumentLink(bandId, ampId, instrumentId) {
        await this.#findOwned(bandId, ampId);
        if (instrumentId) {
            await this.prisma.instrument.updateMany({
                where: { ampId, bandId },
                data: { ampId: null },
            });
            await this.prisma.instrument.update({
                where: { id: instrumentId },
                data: { ampId },
            });
        }
        else {
            await this.prisma.instrument.updateMany({
                where: { ampId, bandId },
                data: { ampId: null },
            });
        }
    }
    async setMics(bandId, amplifierId, micIds) {
        await this.#findOwned(bandId, amplifierId);
        await this.prisma.microphone.updateMany({
            where: { bandId, assignedToType: 'amplifier', assignedToId: amplifierId },
            data: { assignedToType: null, assignedToId: null },
        });
        if (micIds.length > 0) {
            await this.prisma.microphone.updateMany({
                where: { bandId, id: { in: micIds } },
                data: { assignedToType: 'amplifier', assignedToId: amplifierId },
            });
        }
    }
    async #findOwned(bandId, id) {
        const m = await this.prisma.amplifier.findFirst({ where: { id, bandId } });
        if (!m)
            throw new common_1.NotFoundException('Amplifier not found');
        return m;
    }
};
exports.AmplifiersService = AmplifiersService;
exports.AmplifiersService = AmplifiersService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], AmplifiersService);
//# sourceMappingURL=amplifiers.service.js.map
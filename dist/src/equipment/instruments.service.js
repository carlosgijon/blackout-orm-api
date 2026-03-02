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
exports.InstrumentsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
let InstrumentsService = class InstrumentsService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    findAll(bandId) {
        return this.prisma.instrument.findMany({ where: { bandId }, orderBy: { channelOrder: 'asc' } });
    }
    create(bandId, dto) {
        const { id: _id, ...data } = dto;
        return this.prisma.instrument.create({ data: { ...data, bandId } });
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const { id: _id, bandId: _bid, ...data } = dto;
        return this.prisma.instrument.update({ where: { id }, data });
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.instrument.delete({ where: { id } });
    }
    async setMics(bandId, instrumentId, micIds) {
        await this.#findOwned(bandId, instrumentId);
        await this.prisma.microphone.updateMany({
            where: { bandId, assignedToType: 'instrument', assignedToId: instrumentId },
            data: { assignedToType: null, assignedToId: null },
        });
        if (micIds.length > 0) {
            await this.prisma.microphone.updateMany({
                where: { bandId, id: { in: micIds } },
                data: { assignedToType: 'instrument', assignedToId: instrumentId },
            });
        }
    }
    async #findOwned(bandId, id) {
        const m = await this.prisma.instrument.findFirst({ where: { id, bandId } });
        if (!m)
            throw new common_1.NotFoundException('Instrument not found');
        return m;
    }
};
exports.InstrumentsService = InstrumentsService;
exports.InstrumentsService = InstrumentsService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], InstrumentsService);
//# sourceMappingURL=instruments.service.js.map
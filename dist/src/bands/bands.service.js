"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.BandsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
const bcrypt = __importStar(require("bcrypt"));
let BandsService = class BandsService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll() {
        return this.prisma.band.findMany({
            select: { id: true, name: true, slug: true, createdAt: true, _count: { select: { userBands: true } } },
            orderBy: { createdAt: 'asc' },
        });
    }
    async create(dto) {
        const exists = await this.prisma.band.findUnique({ where: { slug: dto.slug } });
        if (exists)
            throw new common_1.BadRequestException(`El slug "${dto.slug}" ya está en uso`);
        const userExists = await this.prisma.user.findUnique({ where: { username: dto.adminUsername } });
        if (userExists)
            throw new common_1.BadRequestException(`El usuario "${dto.adminUsername}" ya existe`);
        const passwordHash = await bcrypt.hash(dto.adminPassword, 10);
        const band = await this.prisma.band.create({ data: { name: dto.name, slug: dto.slug } });
        const user = await this.prisma.user.create({
            data: {
                username: dto.adminUsername,
                passwordHash,
                role: 'admin',
                bandId: band.id,
                userBands: { create: { bandId: band.id, role: 'admin' } },
            },
        });
        return { ...band, adminUser: { id: user.id, username: user.username } };
    }
    async remove(id) {
        const band = await this.prisma.band.findUnique({ where: { id } });
        if (!band)
            throw new common_1.NotFoundException('Banda no encontrada');
        await this.prisma.band.delete({ where: { id } });
    }
    async getMyBand(bandId) {
        const band = await this.prisma.band.findUnique({ where: { id: bandId } });
        if (!band)
            throw new common_1.NotFoundException('Banda no encontrada');
        return band;
    }
    async updateMyBand(bandId, dto) {
        await this.getMyBand(bandId);
        return this.prisma.band.update({ where: { id: bandId }, data: dto });
    }
};
exports.BandsService = BandsService;
exports.BandsService = BandsService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], BandsService);
//# sourceMappingURL=bands.service.js.map
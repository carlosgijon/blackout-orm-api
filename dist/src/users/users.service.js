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
exports.UsersService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
const bcrypt = __importStar(require("bcrypt"));
let UsersService = class UsersService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const users = await this.prisma.user.findMany({
            where: { bandId },
            include: { band: true },
            orderBy: { createdAt: 'asc' },
        });
        return users.map(this.sanitize);
    }
    async create(bandId, dto) {
        const exists = await this.prisma.user.findUnique({ where: { username: dto.username } });
        if (exists)
            throw new common_1.BadRequestException(`El usuario "${dto.username}" ya existe`);
        const passwordHash = await bcrypt.hash(dto.password, 10);
        const user = await this.prisma.user.create({
            data: {
                username: dto.username,
                displayName: dto.displayName,
                passwordHash,
                role: dto.role,
                bandId,
                userBands: { create: { bandId, role: dto.role } },
            },
            include: { band: true },
        });
        return this.sanitize(user);
    }
    async update(bandId, id, dto) {
        const user = await this.prisma.user.findFirst({ where: { id, bandId } });
        if (!user)
            throw new common_1.NotFoundException('Usuario no encontrado');
        if (dto.username && dto.username !== user.username) {
            const exists = await this.prisma.user.findUnique({ where: { username: dto.username } });
            if (exists)
                throw new common_1.BadRequestException(`El usuario "${dto.username}" ya existe`);
        }
        const updated = await this.prisma.user.update({
            where: { id },
            data: dto,
            include: { band: true },
        });
        return this.sanitize(updated);
    }
    async changePassword(bandId, id, newPassword) {
        const user = await this.prisma.user.findFirst({ where: { id, bandId } });
        if (!user)
            throw new common_1.NotFoundException('Usuario no encontrado');
        const passwordHash = await bcrypt.hash(newPassword, 10);
        await this.prisma.user.update({ where: { id }, data: { passwordHash } });
    }
    async remove(bandId, id, requesterId) {
        if (id === requesterId)
            throw new common_1.ForbiddenException('No puedes eliminar tu propio usuario');
        const user = await this.prisma.user.findFirst({ where: { id, bandId } });
        if (!user)
            throw new common_1.NotFoundException('Usuario no encontrado');
        await this.prisma.user.delete({ where: { id } });
    }
    sanitize(user) {
        const { passwordHash: _ph, ...rest } = user;
        return rest;
    }
};
exports.UsersService = UsersService;
exports.UsersService = UsersService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], UsersService);
//# sourceMappingURL=users.service.js.map
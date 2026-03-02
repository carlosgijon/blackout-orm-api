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
exports.AuthService = void 0;
const common_1 = require("@nestjs/common");
const jwt_1 = require("@nestjs/jwt");
const prisma_service_1 = require("../prisma/prisma.service");
const bcrypt = __importStar(require("bcrypt"));
let AuthService = class AuthService {
    prisma;
    jwt;
    constructor(prisma, jwt) {
        this.prisma = prisma;
        this.jwt = jwt;
    }
    async login(dto) {
        const user = await this.prisma.user.findUnique({ where: { username: dto.username } });
        if (!user || !user.isActive)
            throw new common_1.UnauthorizedException('Credenciales incorrectas');
        const valid = await bcrypt.compare(dto.password, user.passwordHash);
        if (!valid)
            throw new common_1.UnauthorizedException('Credenciales incorrectas');
        const memberships = await this.prisma.userBand.findMany({
            where: { userId: user.id },
            include: { band: { select: { id: true, name: true, slug: true, logo: true } } },
        });
        const bands = memberships.map(m => ({
            id: m.band.id,
            name: m.band.name,
            slug: m.band.slug,
            logo: m.band.logo ?? undefined,
            role: m.role,
        }));
        const bandIdForToken = bands.length === 1 ? bands[0].id : null;
        const payload = {
            sub: user.id,
            username: user.username,
            role: user.role,
            bandId: bandIdForToken,
        };
        return {
            token: this.jwt.sign(payload),
            user: this.#sanitize(user),
            bands,
        };
    }
    async selectBand(userId, bandId) {
        const membership = await this.prisma.userBand.findUnique({
            where: { userId_bandId: { userId, bandId } },
            include: { band: { select: { id: true, name: true, slug: true, logo: true } } },
        });
        if (!membership)
            throw new common_1.ForbiddenException('No perteneces a esta banda');
        const user = await this.prisma.user.findUnique({ where: { id: userId } });
        if (!user || !user.isActive)
            throw new common_1.UnauthorizedException('Usuario inactivo');
        const payload = {
            sub: user.id,
            username: user.username,
            role: membership.role,
            bandId,
        };
        return {
            token: this.jwt.sign(payload),
            user: this.#sanitize(user),
            band: {
                id: membership.band.id,
                name: membership.band.name,
                slug: membership.band.slug,
                logo: membership.band.logo ?? undefined,
                role: membership.role,
            },
        };
    }
    async getMe(userId, bandId) {
        const user = await this.prisma.user.findUnique({ where: { id: userId } });
        if (!user)
            throw new common_1.NotFoundException('Usuario no encontrado');
        let band = null;
        if (bandId) {
            const b = await this.prisma.band.findUnique({
                where: { id: bandId },
                select: { id: true, name: true, slug: true, logo: true },
            });
            if (b) {
                const membership = await this.prisma.userBand.findUnique({
                    where: { userId_bandId: { userId, bandId } },
                });
                band = { ...b, logo: b.logo ?? undefined, role: membership?.role ?? user.role };
            }
        }
        return { user: this.#sanitize(user), band };
    }
    #sanitize(user) {
        const { passwordHash: _ph, ...rest } = user;
        return rest;
    }
};
exports.AuthService = AuthService;
exports.AuthService = AuthService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService,
        jwt_1.JwtService])
], AuthService);
//# sourceMappingURL=auth.service.js.map
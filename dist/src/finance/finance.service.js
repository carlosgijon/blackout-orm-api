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
exports.FinanceService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
function toTransaction(t) {
    return {
        id: t.id,
        type: t.type,
        category: t.category,
        amount: t.amount,
        date: t.date,
        description: t.description ?? undefined,
        gigId: t.gigId ?? undefined,
        createdAt: t.createdAt.toISOString(),
    };
}
function toWishListItem(w) {
    return {
        id: w.id,
        name: w.name,
        category: w.category,
        estimatedPrice: w.estimatedPrice ?? undefined,
        priority: w.priority,
        notes: w.notes ?? undefined,
        purchased: w.purchased,
        createdAt: w.createdAt.toISOString(),
    };
}
let FinanceService = class FinanceService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAllTransactions(bandId) {
        const rows = await this.prisma.transaction.findMany({
            where: { bandId },
            orderBy: { date: 'desc' },
        });
        return rows.map(toTransaction);
    }
    async createTransaction(bandId, dto) {
        const { id: _id, createdAt: _ca, ...data } = dto;
        const t = await this.prisma.transaction.create({
            data: { ...data, bandId },
        });
        return toTransaction(t);
    }
    async updateTransaction(bandId, id, dto) {
        await this.#findOwnedTransaction(bandId, id);
        const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
        const t = await this.prisma.transaction.update({
            where: { id },
            data,
        });
        return toTransaction(t);
    }
    async removeTransaction(bandId, id) {
        await this.#findOwnedTransaction(bandId, id);
        await this.prisma.transaction.delete({ where: { id } });
    }
    async #findOwnedTransaction(bandId, id) {
        const t = await this.prisma.transaction.findUnique({ where: { id } });
        if (!t || t.bandId !== bandId)
            throw new common_1.NotFoundException('Transaction not found');
        return t;
    }
    async findAllWishList(bandId) {
        const rows = await this.prisma.wishListItem.findMany({
            where: { bandId },
            orderBy: { createdAt: 'asc' },
        });
        return rows.map(toWishListItem);
    }
    async createWishListItem(bandId, dto) {
        const { id: _id, createdAt: _ca, ...data } = dto;
        const w = await this.prisma.wishListItem.create({
            data: { ...data, bandId },
        });
        return toWishListItem(w);
    }
    async updateWishListItem(bandId, id, dto) {
        await this.#findOwnedWishItem(bandId, id);
        const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
        const w = await this.prisma.wishListItem.update({
            where: { id },
            data,
        });
        return toWishListItem(w);
    }
    async removeWishListItem(bandId, id) {
        await this.#findOwnedWishItem(bandId, id);
        await this.prisma.wishListItem.delete({ where: { id } });
    }
    async #findOwnedWishItem(bandId, id) {
        const w = await this.prisma.wishListItem.findUnique({ where: { id } });
        if (!w || w.bandId !== bandId)
            throw new common_1.NotFoundException('Wish list item not found');
        return w;
    }
};
exports.FinanceService = FinanceService;
exports.FinanceService = FinanceService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], FinanceService);
//# sourceMappingURL=finance.service.js.map
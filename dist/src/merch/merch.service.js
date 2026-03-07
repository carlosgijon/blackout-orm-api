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
exports.MerchService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma/prisma.service");
function toMerchItem(m) {
    return {
        id: m.id,
        name: m.name,
        type: m.type,
        productionCost: m.productionCost,
        batchSize: m.batchSize,
        sellingPrice: m.sellingPrice,
        fixedCosts: m.fixedCosts,
        stock: m.stock ?? 0,
        hasSizes: m.hasSizes ?? false,
        stockSizes: m.stockSizes ?? undefined,
        notes: m.notes ?? undefined,
        createdAt: m.createdAt.toISOString(),
    };
}
let MerchService = class MerchService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async findAll(bandId) {
        const rows = await this.prisma.merchItem.findMany({
            where: { bandId },
            orderBy: { createdAt: 'asc' },
        });
        return rows.map(toMerchItem);
    }
    async create(bandId, dto) {
        const { id: _id, createdAt: _ca, ...data } = dto;
        if (data.hasSizes && data.stockSizes) {
            data.stock = Object.values(data.stockSizes).reduce((a, b) => a + b, 0);
        }
        const m = await this.prisma.merchItem.create({
            data: { ...data, bandId },
        });
        return toMerchItem(m);
    }
    async update(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const { id: _id, createdAt: _ca, bandId: _bid, ...data } = dto;
        if (data.hasSizes && data.stockSizes) {
            data.stock = Object.values(data.stockSizes).reduce((a, b) => a + b, 0);
        }
        const m = await this.prisma.merchItem.update({
            where: { id },
            data,
        });
        return toMerchItem(m);
    }
    async remove(bandId, id) {
        await this.#findOwned(bandId, id);
        await this.prisma.merchItem.delete({ where: { id } });
    }
    async restock(bandId, id, dto) {
        await this.#findOwned(bandId, id);
        const updateData = {};
        if (dto.stockSizes !== undefined) {
            updateData.stockSizes = dto.stockSizes;
            updateData.stock = Object.values(dto.stockSizes).reduce((a, b) => a + b, 0);
        }
        else if (dto.stock !== undefined) {
            updateData.stock = dto.stock;
        }
        const m = await this.prisma.merchItem.update({ where: { id }, data: updateData });
        return toMerchItem(m);
    }
    async sell(bandId, id, dto) {
        const item = await this.#findOwned(bandId, id);
        if (dto.quantity <= 0)
            throw new common_1.BadRequestException('La cantidad debe ser mayor que 0');
        let stockUpdate;
        if (item.hasSizes && dto.size) {
            const sizes = item.stockSizes ?? {};
            const sizeStock = sizes[dto.size] ?? 0;
            if (sizeStock < dto.quantity) {
                throw new common_1.BadRequestException(`Stock insuficiente en talla ${dto.size} (disponible: ${sizeStock})`);
            }
            const newSizes = { ...sizes, [dto.size]: sizeStock - dto.quantity };
            const newTotal = Object.values(newSizes).reduce((a, b) => a + b, 0);
            stockUpdate = { stockSizes: newSizes, stock: newTotal };
        }
        else {
            if (item.stock < dto.quantity) {
                throw new common_1.BadRequestException(`Stock insuficiente (disponible: ${item.stock})`);
            }
            stockUpdate = { stock: { decrement: dto.quantity } };
        }
        const sizeLabel = dto.size ? ` [${dto.size}]` : '';
        const totalAmount = dto.quantity * dto.unitPrice;
        const [updatedItem, transaction] = await this.prisma.$transaction([
            this.prisma.merchItem.update({ where: { id }, data: stockUpdate }),
            this.prisma.transaction.create({
                data: {
                    bandId,
                    type: 'income',
                    category: 'merch_sales',
                    amount: totalAmount,
                    date: dto.date,
                    description: `Venta merch: ${dto.quantity}× ${item.name}${sizeLabel} @ ${dto.unitPrice}€${dto.notes ? ` — ${dto.notes}` : ''}`,
                },
            }),
        ]);
        return {
            item: toMerchItem(updatedItem),
            transaction: {
                id: transaction.id,
                type: transaction.type,
                category: transaction.category,
                amount: transaction.amount,
                date: transaction.date,
                description: transaction.description,
                createdAt: transaction.createdAt.toISOString(),
            },
        };
    }
    async #findOwned(bandId, id) {
        const m = await this.prisma.merchItem.findUnique({ where: { id } });
        if (!m || m.bandId !== bandId)
            throw new common_1.NotFoundException('Merch item not found');
        return m;
    }
};
exports.MerchService = MerchService;
exports.MerchService = MerchService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], MerchService);
//# sourceMappingURL=merch.service.js.map
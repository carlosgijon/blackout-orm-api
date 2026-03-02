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
Object.defineProperty(exports, "__esModule", { value: true });
require("dotenv/config");
const client_1 = require("@prisma/client");
const bcrypt = __importStar(require("bcrypt"));
const prisma = new client_1.PrismaClient();
async function main() {
    const existing = await prisma.user.findUnique({ where: { username: 'superadmin' } });
    if (!existing) {
        const passwordHash = await bcrypt.hash('superadmin123', 10);
        await prisma.user.create({
            data: { username: 'superadmin', displayName: 'Super Admin', passwordHash, role: 'superadmin', bandId: null },
        });
        console.log('✓ Superadmin created: superadmin / superadmin123');
    }
    else {
        console.log('✓ Superadmin already exists');
    }
    let band = await prisma.band.findUnique({ where: { slug: 'blackout' } });
    if (!band) {
        band = await prisma.band.create({ data: { name: 'Blackout', slug: 'blackout' } });
        console.log(`✓ Band created: Blackout (id: ${band.id})`);
    }
    else {
        console.log(`✓ Band already exists: Blackout (id: ${band.id})`);
    }
    let admin = await prisma.user.findUnique({ where: { username: 'admin' } });
    if (!admin) {
        const passwordHash = await bcrypt.hash('admin123', 10);
        admin = await prisma.user.create({
            data: { username: 'admin', displayName: 'Administrador', passwordHash, role: 'admin', bandId: band.id },
        });
        console.log(`✓ Admin created: admin / admin123`);
    }
    else {
        if (admin.bandId !== band.id) {
            await prisma.user.update({ where: { id: admin.id }, data: { bandId: band.id } });
        }
        console.log(`✓ Admin already exists`);
    }
    const usersWithBand = await prisma.user.findMany({ where: { bandId: { not: null } } });
    for (const user of usersWithBand) {
        const exists = await prisma.userBand.findUnique({
            where: { userId_bandId: { userId: user.id, bandId: user.bandId } },
        });
        if (!exists) {
            await prisma.userBand.create({
                data: { userId: user.id, bandId: user.bandId, role: user.role },
            });
            console.log(`✓ UserBand created for ${user.username} → band ${user.bandId}`);
        }
    }
    console.log('✓ UserBand sync complete');
}
main().catch(console.error).finally(() => prisma.$disconnect());
//# sourceMappingURL=seed.js.map
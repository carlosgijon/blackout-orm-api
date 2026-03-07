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
    const superHash = await bcrypt.hash('superadmin1234', 10);
    await prisma.user.upsert({
        where: { username: 'superadmin' },
        update: { passwordHash: superHash, role: 'superadmin', displayName: 'Super Admin' },
        create: { username: 'superadmin', displayName: 'Super Admin', passwordHash: superHash, role: 'superadmin', bandId: null },
    });
    console.log('✓ superadmin / superadmin1234');
    const band = await prisma.band.upsert({
        where: { slug: 'blackout' },
        update: {},
        create: { name: 'Blackout', slug: 'blackout' },
    });
    console.log(`✓ Band: Blackout (${band.id})`);
    const adminHash = await bcrypt.hash('admin123', 10);
    const admin = await prisma.user.upsert({
        where: { username: 'admin' },
        update: { passwordHash: adminHash, role: 'admin', bandId: band.id, displayName: 'Administrador' },
        create: { username: 'admin', displayName: 'Administrador', passwordHash: adminHash, role: 'admin', bandId: band.id },
    });
    console.log(`✓ admin / admin123`);
    await prisma.userBand.upsert({
        where: { userId_bandId: { userId: admin.id, bandId: band.id } },
        update: { role: 'admin' },
        create: { userId: admin.id, bandId: band.id, role: 'admin' },
    });
    console.log('✓ UserBand synced');
}
main().catch(console.error).finally(() => prisma.$disconnect());
//# sourceMappingURL=seed.js.map
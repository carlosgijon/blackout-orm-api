import 'dotenv/config';
import { PrismaClient } from '@prisma/client';
import * as bcrypt from 'bcrypt';

const prisma = new PrismaClient();

async function main() {
  // -- Superadmin (no band) --------------------------------------------------
  const superHash = await bcrypt.hash('superadmin1234', 10);
  await prisma.user.upsert({
    where: { username: 'superadmin' },
    update: { passwordHash: superHash, role: 'superadmin', displayName: 'Super Admin' },
    create: { username: 'superadmin', displayName: 'Super Admin', passwordHash: superHash, role: 'superadmin', bandId: null },
  });
  console.log('✓ superadmin / superadmin1234');

  // -- Blackout band ---------------------------------------------------------
  const band = await prisma.band.upsert({
    where: { slug: 'blackout' },
    update: {},
    create: { name: 'Blackout', slug: 'blackout' },
  });
  console.log(`✓ Band: Blackout (${band.id})`);

  // -- Blackout admin user ---------------------------------------------------
  const adminHash = await bcrypt.hash('admin123', 10);
  const admin = await prisma.user.upsert({
    where: { username: 'admin' },
    update: { passwordHash: adminHash, role: 'admin', bandId: band.id, displayName: 'Administrador' },
    create: { username: 'admin', displayName: 'Administrador', passwordHash: adminHash, role: 'admin', bandId: band.id },
  });
  console.log(`✓ admin / admin123`);

  // -- UserBand membership ---------------------------------------------------
  await prisma.userBand.upsert({
    where: { userId_bandId: { userId: admin.id, bandId: band.id } },
    update: { role: 'admin' },
    create: { userId: admin.id, bandId: band.id, role: 'admin' },
  });
  console.log('✓ UserBand synced');
}

main().catch(console.error).finally(() => prisma.$disconnect());

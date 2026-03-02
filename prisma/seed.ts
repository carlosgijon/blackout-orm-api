import 'dotenv/config';
import { PrismaClient } from '@prisma/client';
import * as bcrypt from 'bcrypt';

const prisma = new PrismaClient();

async function main() {
  // -- Superadmin (no band) --------------------------------------------------
  const existing = await prisma.user.findUnique({ where: { username: 'superadmin' } });
  if (!existing) {
    const passwordHash = await bcrypt.hash('superadmin123', 10);
    await prisma.user.create({
      data: { username: 'superadmin', displayName: 'Super Admin', passwordHash, role: 'superadmin', bandId: null },
    });
    console.log('✓ Superadmin created: superadmin / superadmin123');
  } else {
    console.log('✓ Superadmin already exists');
  }

  // -- Blackout band ---------------------------------------------------------
  let band = await prisma.band.findUnique({ where: { slug: 'blackout' } });
  if (!band) {
    band = await prisma.band.create({ data: { name: 'Blackout', slug: 'blackout' } });
    console.log(`✓ Band created: Blackout (id: ${band.id})`);
  } else {
    console.log(`✓ Band already exists: Blackout (id: ${band.id})`);
  }

  // -- Blackout admin user ---------------------------------------------------
  let admin = await prisma.user.findUnique({ where: { username: 'admin' } });
  if (!admin) {
    const passwordHash = await bcrypt.hash('admin123', 10);
    admin = await prisma.user.create({
      data: { username: 'admin', displayName: 'Administrador', passwordHash, role: 'admin', bandId: band.id },
    });
    console.log(`✓ Admin created: admin / admin123`);
  } else {
    if (admin.bandId !== band.id) {
      await prisma.user.update({ where: { id: admin.id }, data: { bandId: band.id } });
    }
    console.log(`✓ Admin already exists`);
  }

  // -- Sync UserBand for all users with bandId --------------------------------
  const usersWithBand = await prisma.user.findMany({ where: { bandId: { not: null } } });
  for (const user of usersWithBand) {
    const exists = await prisma.userBand.findUnique({
      where: { userId_bandId: { userId: user.id, bandId: user.bandId! } },
    });
    if (!exists) {
      await prisma.userBand.create({
        data: { userId: user.id, bandId: user.bandId!, role: user.role },
      });
      console.log(`✓ UserBand created for ${user.username} → band ${user.bandId}`);
    }
  }
  console.log('✓ UserBand sync complete');
}

main().catch(console.error).finally(() => prisma.$disconnect());

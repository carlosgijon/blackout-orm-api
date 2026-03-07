const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcrypt');

async function main() {
  const p = new PrismaClient();

  const existingSuperadmin = await p.user.findUnique({ where: { username: 'superadmin' } });
  if (!existingSuperadmin) {
    const h = await bcrypt.hash('superadmin123', 10);
    await p.user.create({
      data: { username: 'superadmin', displayName: 'Super Admin', passwordHash: h, role: 'superadmin', bandId: null },
    });
    console.log('superadmin creado');
  } else {
    console.log('superadmin ya existe');
  }

  let band = await p.band.findUnique({ where: { slug: 'blackout' } });
  if (!band) {
    band = await p.band.create({ data: { name: 'Blackout', slug: 'blackout' } });
    console.log('banda creada');
  } else {
    console.log('banda ya existe');
  }

  const existingAdmin = await p.user.findUnique({ where: { username: 'admin' } });
  if (!existingAdmin) {
    const h = await bcrypt.hash('admin123', 10);
    const admin = await p.user.create({
      data: { username: 'admin', displayName: 'Administrador', passwordHash: h, role: 'admin', bandId: band.id },
    });
    await p.userBand.create({ data: { userId: admin.id, bandId: band.id, role: 'admin' } });
    console.log('admin creado');
  } else {
    console.log('admin ya existe');
  }

  await p.$disconnect();
}

main().catch(console.error);

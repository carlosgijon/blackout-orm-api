import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.setGlobalPrefix('api');

  app.useGlobalPipes(
    new ValidationPipe({ whitelist: true, forbidNonWhitelisted: true, transform: true }),
  );

  app.enableCors({
    origin: [
      'http://localhost:4200',  // Angular dev server
      'capacitor://localhost',  // Capacitor Android/iOS
      'https://localhost',      // Capacitor con androidScheme: 'https'
      'http://localhost',       // Capacitor fallback
    ],
    credentials: true,
  });

  // Swagger / OpenAPI — available at /docs
  const swaggerConfig = new DocumentBuilder()
    .setTitle('Blackout ORM API')
    .setDescription('API para la gestión de bandas, usuarios y setlists')
    .setVersion('1.0')
    .addBearerAuth({ type: 'http', scheme: 'bearer', bearerFormat: 'JWT' }, 'JWT')
    .build();
  const document = SwaggerModule.createDocument(app, swaggerConfig);
  SwaggerModule.setup('docs', app, document, {
    swaggerOptions: { persistAuthorization: true },
  });

  const port = process.env.PORT ?? 3000;
  await app.listen(port);
  console.log(`API running on  http://localhost:${port}/api`);
  console.log(`Swagger docs at http://localhost:${port}/docs`);
}
bootstrap();

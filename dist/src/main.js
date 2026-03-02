"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const core_1 = require("@nestjs/core");
const common_1 = require("@nestjs/common");
const swagger_1 = require("@nestjs/swagger");
const app_module_1 = require("./app.module");
async function bootstrap() {
    const app = await core_1.NestFactory.create(app_module_1.AppModule);
    app.setGlobalPrefix('api');
    app.useGlobalPipes(new common_1.ValidationPipe({ whitelist: true, forbidNonWhitelisted: true, transform: true }));
    app.enableCors({
        origin: [
            'http://localhost:4200',
            'tauri://localhost',
            'https://tauri.localhost',
        ],
        credentials: true,
    });
    const swaggerConfig = new swagger_1.DocumentBuilder()
        .setTitle('Blackout ORM API')
        .setDescription('API para la gestión de bandas, usuarios y setlists')
        .setVersion('1.0')
        .addBearerAuth({ type: 'http', scheme: 'bearer', bearerFormat: 'JWT' }, 'JWT')
        .build();
    const document = swagger_1.SwaggerModule.createDocument(app, swaggerConfig);
    swagger_1.SwaggerModule.setup('docs', app, document, {
        swaggerOptions: { persistAuthorization: true },
    });
    const port = process.env.PORT ?? 3000;
    await app.listen(port);
    console.log(`API running on  http://localhost:${port}/api`);
    console.log(`Swagger docs at http://localhost:${port}/docs`);
}
bootstrap();
//# sourceMappingURL=main.js.map
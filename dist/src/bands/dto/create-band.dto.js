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
exports.CreateBandDto = void 0;
const class_validator_1 = require("class-validator");
const swagger_1 = require("@nestjs/swagger");
class CreateBandDto {
    name;
    slug;
    adminUsername;
    adminPassword;
}
exports.CreateBandDto = CreateBandDto;
__decorate([
    (0, swagger_1.ApiProperty)({ example: 'Blackout' }),
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.MinLength)(2),
    __metadata("design:type", String)
], CreateBandDto.prototype, "name", void 0);
__decorate([
    (0, swagger_1.ApiProperty)({ example: 'blackout', description: 'Solo minúsculas, números y guiones' }),
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.Matches)(/^[a-z0-9-]+$/, { message: 'El slug solo puede contener minúsculas, números y guiones' }),
    __metadata("design:type", String)
], CreateBandDto.prototype, "slug", void 0);
__decorate([
    (0, swagger_1.ApiProperty)({ example: 'admin_blackout', description: 'Username del administrador de la banda' }),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], CreateBandDto.prototype, "adminUsername", void 0);
__decorate([
    (0, swagger_1.ApiProperty)({ example: 'password123', minLength: 4 }),
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.MinLength)(4),
    __metadata("design:type", String)
], CreateBandDto.prototype, "adminPassword", void 0);
//# sourceMappingURL=create-band.dto.js.map
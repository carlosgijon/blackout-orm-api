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
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.BandsController = void 0;
const common_1 = require("@nestjs/common");
const swagger_1 = require("@nestjs/swagger");
const bands_service_1 = require("./bands.service");
const create_band_dto_1 = require("./dto/create-band.dto");
const jwt_guard_1 = require("../auth/guards/jwt.guard");
const admin_guard_1 = require("../auth/guards/admin.guard");
const band_guard_1 = require("../common/guards/band.guard");
const current_user_decorator_1 = require("../common/decorators/current-user.decorator");
let BandsController = class BandsController {
    bands;
    constructor(bands) {
        this.bands = bands;
    }
    findAll() {
        return this.bands.findAll();
    }
    create(dto) {
        return this.bands.create(dto);
    }
    remove(id) {
        return this.bands.remove(id);
    }
    getMyBand(user) {
        return this.bands.getMyBand(user.bandId);
    }
    updateMyBand(user, dto) {
        return this.bands.updateMyBand(user.bandId, dto);
    }
};
exports.BandsController = BandsController;
__decorate([
    (0, common_1.Get)(),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, admin_guard_1.SuperAdminGuard),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", void 0)
], BandsController.prototype, "findAll", null);
__decorate([
    (0, common_1.Post)(),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, admin_guard_1.SuperAdminGuard),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [create_band_dto_1.CreateBandDto]),
    __metadata("design:returntype", void 0)
], BandsController.prototype, "create", null);
__decorate([
    (0, common_1.Delete)(':id'),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, admin_guard_1.SuperAdminGuard),
    __param(0, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", void 0)
], BandsController.prototype, "remove", null);
__decorate([
    (0, common_1.Get)('mine'),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, band_guard_1.BandGuard),
    (0, swagger_1.ApiOperation)({ summary: 'Obtener información de la banda actual' }),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", void 0)
], BandsController.prototype, "getMyBand", null);
__decorate([
    (0, common_1.Put)('mine'),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, band_guard_1.BandGuard, admin_guard_1.AdminGuard),
    (0, swagger_1.ApiOperation)({ summary: 'Actualizar nombre y logo de la banda' }),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, Object]),
    __metadata("design:returntype", void 0)
], BandsController.prototype, "updateMyBand", null);
exports.BandsController = BandsController = __decorate([
    (0, swagger_1.ApiTags)('bands'),
    (0, swagger_1.ApiBearerAuth)('JWT'),
    (0, common_1.Controller)('bands'),
    __metadata("design:paramtypes", [bands_service_1.BandsService])
], BandsController);
//# sourceMappingURL=bands.controller.js.map
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
exports.VenuesController = void 0;
const common_1 = require("@nestjs/common");
const swagger_1 = require("@nestjs/swagger");
const venues_service_1 = require("./venues.service");
const jwt_guard_1 = require("../auth/guards/jwt.guard");
const band_guard_1 = require("../common/guards/band.guard");
const current_user_decorator_1 = require("../common/decorators/current-user.decorator");
let VenuesController = class VenuesController {
    venues;
    constructor(venues) {
        this.venues = venues;
    }
    findAll(user) { return this.venues.findAll(user.bandId); }
    create(user, dto) { return this.venues.create(user.bandId, dto); }
    update(user, id, dto) {
        return this.venues.update(user.bandId, id, dto);
    }
    remove(user, id) { return this.venues.remove(user.bandId, id); }
};
exports.VenuesController = VenuesController;
__decorate([
    (0, common_1.Get)(),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", void 0)
], VenuesController.prototype, "findAll", null);
__decorate([
    (0, common_1.Post)(),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, Object]),
    __metadata("design:returntype", void 0)
], VenuesController.prototype, "create", null);
__decorate([
    (0, common_1.Put)(':id'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('id')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], VenuesController.prototype, "update", null);
__decorate([
    (0, common_1.Delete)(':id'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], VenuesController.prototype, "remove", null);
exports.VenuesController = VenuesController = __decorate([
    (0, swagger_1.ApiTags)('venues'),
    (0, swagger_1.ApiBearerAuth)('JWT'),
    (0, common_1.Controller)('venues'),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, band_guard_1.BandGuard),
    __metadata("design:paramtypes", [venues_service_1.VenuesService])
], VenuesController);
//# sourceMappingURL=venues.controller.js.map
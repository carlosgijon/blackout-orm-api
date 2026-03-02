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
exports.GigsController = void 0;
const common_1 = require("@nestjs/common");
const swagger_1 = require("@nestjs/swagger");
const gigs_service_1 = require("./gigs.service");
const jwt_guard_1 = require("../auth/guards/jwt.guard");
const band_guard_1 = require("../common/guards/band.guard");
const current_user_decorator_1 = require("../common/decorators/current-user.decorator");
let GigsController = class GigsController {
    gigs;
    constructor(gigs) {
        this.gigs = gigs;
    }
    findAll(user) { return this.gigs.findAll(user.bandId); }
    create(user, dto) { return this.gigs.create(user.bandId, dto); }
    update(user, id, dto) {
        return this.gigs.update(user.bandId, id, dto);
    }
    updateStatus(user, id, dto) {
        return this.gigs.updateStatus(user.bandId, id, dto.status);
    }
    updateFollowUp(user, id, dto) {
        return this.gigs.updateFollowUp(user.bandId, id, dto);
    }
    remove(user, id) { return this.gigs.remove(user.bandId, id); }
    getContacts(user, gigId) {
        return this.gigs.getContacts(user.bandId, gigId);
    }
    createContact(user, gigId, dto) {
        return this.gigs.createContact(user.bandId, gigId, dto);
    }
    removeContact(user, gigId, contactId) {
        return this.gigs.removeContact(user.bandId, gigId, contactId);
    }
    getChecklists(user, gigId) {
        return this.gigs.getChecklists(user.bandId, gigId);
    }
    createChecklist(user, gigId, dto) {
        return this.gigs.createChecklist(user.bandId, gigId, dto);
    }
    removeChecklist(user, gigId, checklistId) {
        return this.gigs.removeChecklist(user.bandId, gigId, checklistId);
    }
    getItems(user, checklistId) {
        return this.gigs.getItems(user.bandId, checklistId);
    }
    createItem(user, checklistId, dto) {
        return this.gigs.createItem(user.bandId, checklistId, dto);
    }
    updateItem(user, checklistId, itemId, dto) {
        return this.gigs.updateItem(user.bandId, checklistId, itemId, dto);
    }
    removeItem(user, checklistId, itemId) {
        return this.gigs.removeItem(user.bandId, checklistId, itemId);
    }
    resetItems(user, checklistId) {
        return this.gigs.resetItems(user.bandId, checklistId);
    }
    removeContactById(user, contactId) {
        return this.gigs.removeContactById(user.bandId, contactId);
    }
    removeChecklistById(user, checklistId) {
        return this.gigs.removeChecklistById(user.bandId, checklistId);
    }
    removeItemById(user, itemId) {
        return this.gigs.removeItemById(user.bandId, itemId);
    }
};
exports.GigsController = GigsController;
__decorate([
    (0, common_1.Get)(),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "findAll", null);
__decorate([
    (0, common_1.Post)(),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "create", null);
__decorate([
    (0, common_1.Put)(':id'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('id')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "update", null);
__decorate([
    (0, common_1.Patch)(':id/status'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('id')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "updateStatus", null);
__decorate([
    (0, common_1.Patch)(':id/follow-up'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('id')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "updateFollowUp", null);
__decorate([
    (0, common_1.Delete)(':id'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "remove", null);
__decorate([
    (0, common_1.Get)(':gigId/contacts'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('gigId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "getContacts", null);
__decorate([
    (0, common_1.Post)(':gigId/contacts'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('gigId')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "createContact", null);
__decorate([
    (0, common_1.Delete)(':gigId/contacts/:contactId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('gigId')),
    __param(2, (0, common_1.Param)('contactId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "removeContact", null);
__decorate([
    (0, common_1.Get)(':gigId/checklists'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('gigId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "getChecklists", null);
__decorate([
    (0, common_1.Post)(':gigId/checklists'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('gigId')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "createChecklist", null);
__decorate([
    (0, common_1.Delete)(':gigId/checklists/:checklistId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('gigId')),
    __param(2, (0, common_1.Param)('checklistId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "removeChecklist", null);
__decorate([
    (0, common_1.Get)('checklists/:checklistId/items'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('checklistId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "getItems", null);
__decorate([
    (0, common_1.Post)('checklists/:checklistId/items'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('checklistId')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "createItem", null);
__decorate([
    (0, common_1.Put)('checklists/:checklistId/items/:itemId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('checklistId')),
    __param(2, (0, common_1.Param)('itemId')),
    __param(3, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, String, Object]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "updateItem", null);
__decorate([
    (0, common_1.Delete)('checklists/:checklistId/items/:itemId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('checklistId')),
    __param(2, (0, common_1.Param)('itemId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "removeItem", null);
__decorate([
    (0, common_1.Post)('checklists/:checklistId/reset'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('checklistId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "resetItems", null);
__decorate([
    (0, common_1.Delete)('contacts/:contactId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('contactId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "removeContactById", null);
__decorate([
    (0, common_1.Delete)('checklists/:checklistId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('checklistId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "removeChecklistById", null);
__decorate([
    (0, common_1.Delete)('checklist-items/:itemId'),
    __param(0, (0, current_user_decorator_1.CurrentUser)()),
    __param(1, (0, common_1.Param)('itemId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], GigsController.prototype, "removeItemById", null);
exports.GigsController = GigsController = __decorate([
    (0, swagger_1.ApiTags)('gigs'),
    (0, swagger_1.ApiBearerAuth)('JWT'),
    (0, common_1.Controller)('gigs'),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard, band_guard_1.BandGuard),
    __metadata("design:paramtypes", [gigs_service_1.GigsService])
], GigsController);
//# sourceMappingURL=gigs.controller.js.map
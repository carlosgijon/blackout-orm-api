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
exports.VotesController = void 0;
const common_1 = require("@nestjs/common");
const jwt_guard_1 = require("../auth/guards/jwt.guard");
const votes_service_1 = require("./votes.service");
let VotesController = class VotesController {
    votesService;
    constructor(votesService) {
        this.votesService = votesService;
    }
    getSession(req, playlistId) {
        return this.votesService.getSession(req.user.bandId, playlistId);
    }
    createSession(req, body) {
        return this.votesService.createSession(req.user.bandId, body.playlistId, body.title);
    }
    closeSession(req, id) {
        return this.votesService.closeSession(req.user.bandId, id);
    }
    reopenSession(req, id) {
        return this.votesService.reopenSession(req.user.bandId, id);
    }
    deleteSession(req, id) {
        return this.votesService.deleteSession(req.user.bandId, id);
    }
    castVote(req, id, body) {
        return this.votesService.castVote(req.user.bandId, id, body.voterName, body.orderedIds);
    }
    getResults(req, id) {
        return this.votesService.getResults(req.user.bandId, id);
    }
};
exports.VotesController = VotesController;
__decorate([
    (0, common_1.Get)(),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Query)('playlistId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "getSession", null);
__decorate([
    (0, common_1.Post)(),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, Object]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "createSession", null);
__decorate([
    (0, common_1.Put)(':id/close'),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "closeSession", null);
__decorate([
    (0, common_1.Put)(':id/reopen'),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "reopenSession", null);
__decorate([
    (0, common_1.Delete)(':id'),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "deleteSession", null);
__decorate([
    (0, common_1.Post)(':id/votes'),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Param)('id')),
    __param(2, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String, Object]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "castVote", null);
__decorate([
    (0, common_1.Get)(':id/results'),
    __param(0, (0, common_1.Request)()),
    __param(1, (0, common_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object, String]),
    __metadata("design:returntype", void 0)
], VotesController.prototype, "getResults", null);
exports.VotesController = VotesController = __decorate([
    (0, common_1.Controller)('vote-sessions'),
    (0, common_1.UseGuards)(jwt_guard_1.JwtAuthGuard),
    __metadata("design:paramtypes", [votes_service_1.VotesService])
], VotesController);
//# sourceMappingURL=votes.controller.js.map
"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MerchModule = void 0;
const common_1 = require("@nestjs/common");
const merch_service_1 = require("./merch.service");
const merch_controller_1 = require("./merch.controller");
let MerchModule = class MerchModule {
};
exports.MerchModule = MerchModule;
exports.MerchModule = MerchModule = __decorate([
    (0, common_1.Module)({
        controllers: [merch_controller_1.MerchController],
        providers: [merch_service_1.MerchService],
    })
], MerchModule);
//# sourceMappingURL=merch.module.js.map
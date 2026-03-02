"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.EquipmentModule = void 0;
const common_1 = require("@nestjs/common");
const members_service_1 = require("./members.service");
const microphones_service_1 = require("./microphones.service");
const instruments_service_1 = require("./instruments.service");
const amplifiers_service_1 = require("./amplifiers.service");
const pa_service_1 = require("./pa.service");
const members_controller_1 = require("./members.controller");
const microphones_controller_1 = require("./microphones.controller");
const instruments_controller_1 = require("./instruments.controller");
const amplifiers_controller_1 = require("./amplifiers.controller");
const pa_controller_1 = require("./pa.controller");
const channel_list_controller_1 = require("./channel-list.controller");
const channel_list_service_1 = require("./channel-list.service");
let EquipmentModule = class EquipmentModule {
};
exports.EquipmentModule = EquipmentModule;
exports.EquipmentModule = EquipmentModule = __decorate([
    (0, common_1.Module)({
        controllers: [
            members_controller_1.MembersController,
            microphones_controller_1.MicrophonesController,
            instruments_controller_1.InstrumentsController,
            amplifiers_controller_1.AmplifiersController,
            pa_controller_1.PaController,
            channel_list_controller_1.ChannelListController,
        ],
        providers: [
            members_service_1.MembersService,
            microphones_service_1.MicrophonesService,
            instruments_service_1.InstrumentsService,
            amplifiers_service_1.AmplifiersService,
            pa_service_1.PaService,
            channel_list_service_1.ChannelListService,
        ],
    })
], EquipmentModule);
//# sourceMappingURL=equipment.module.js.map
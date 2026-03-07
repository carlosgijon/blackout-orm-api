"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.BpmService = void 0;
const common_1 = require("@nestjs/common");
let BpmService = class BpmService {
    async lookup(title, artist) {
        const query = encodeURIComponent(`${title} ${artist}`);
        const searchUrl = `https://api.deezer.com/search?q=${query}&limit=1`;
        const searchRes = await fetch(searchUrl);
        if (!searchRes.ok)
            return null;
        const searchData = (await searchRes.json());
        const track = searchData?.data?.[0];
        if (!track)
            return null;
        if (track.bpm && track.bpm > 0)
            return track.bpm;
        if (track.id) {
            const trackRes = await fetch(`https://api.deezer.com/track/${track.id}`);
            if (trackRes.ok) {
                const trackData = (await trackRes.json());
                if (trackData.bpm && trackData.bpm > 0)
                    return trackData.bpm;
            }
        }
        return null;
    }
};
exports.BpmService = BpmService;
exports.BpmService = BpmService = __decorate([
    (0, common_1.Injectable)()
], BpmService);
//# sourceMappingURL=bpm.service.js.map
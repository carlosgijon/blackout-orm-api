import { SettingsService } from './settings.service';
export declare class SettingsController {
    private readonly settings;
    constructor(settings: SettingsService);
    get(user: any): Promise<{
        theme: string;
        bpmApiKey?: string;
    }>;
    set(user: any, dto: {
        theme?: string;
        bpmApiKey?: string;
    }): Promise<void>;
}

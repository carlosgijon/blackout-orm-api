import { AiService, SetlistResult } from './ai.service';
export declare class AiController {
    private readonly ai;
    constructor(ai: AiService);
    generateSetlist(dto: {
        songs: any[];
        preferences?: string;
    }): Promise<SetlistResult>;
}

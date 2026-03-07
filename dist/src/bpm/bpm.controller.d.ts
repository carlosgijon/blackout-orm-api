import { BpmService } from './bpm.service';
export declare class BpmController {
    private readonly bpmService;
    constructor(bpmService: BpmService);
    lookup(title: string, artist: string): Promise<{
        bpm: number | null;
    }>;
}

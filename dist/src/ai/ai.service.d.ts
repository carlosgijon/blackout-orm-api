interface SongInput {
    id: string;
    title: string;
    artist: string;
    tempo?: number;
    style?: string;
    startNote?: string;
    endNote?: string;
    duration?: number;
}
export interface SetlistResult {
    orderedIds: string[];
    joinAfter: string[];
    bisAfterSongId: string | null;
    explanation: string;
}
export declare class AiService {
    generateSetlist(songs: SongInput[], preferences: string): Promise<SetlistResult>;
}
export {};

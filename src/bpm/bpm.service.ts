import { Injectable } from '@nestjs/common';

@Injectable()
export class BpmService {
  async lookup(title: string, artist: string): Promise<number | null> {
    const query = encodeURIComponent(`${title} ${artist}`);
    const searchUrl = `https://api.deezer.com/search?q=${query}&limit=1`;
    const searchRes = await fetch(searchUrl);
    if (!searchRes.ok) return null;
    const searchData = (await searchRes.json()) as {
      data?: { id?: number; bpm?: number }[];
    };
    const track = searchData?.data?.[0];
    if (!track) return null;

    // Search results rarely include bpm anymore; try the full track endpoint
    if (track.bpm && track.bpm > 0) return track.bpm;

    if (track.id) {
      const trackRes = await fetch(`https://api.deezer.com/track/${track.id}`);
      if (trackRes.ok) {
        const trackData = (await trackRes.json()) as { bpm?: number };
        if (trackData.bpm && trackData.bpm > 0) return trackData.bpm;
      }
    }

    return null;
  }
}

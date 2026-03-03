import { Injectable } from '@nestjs/common';

@Injectable()
export class BpmService {
  async lookup(title: string, artist: string): Promise<number | null> {
    const query = encodeURIComponent(`${title} ${artist}`);
    const url = `https://api.deezer.com/search?q=${query}&limit=1`;
    const res = await fetch(url);
    if (!res.ok) return null;
    const data = (await res.json()) as { data?: { bpm?: number }[] };
    return data?.data?.[0]?.bpm ?? null;
  }
}

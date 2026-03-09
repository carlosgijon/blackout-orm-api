import { Injectable, BadRequestException } from '@nestjs/common';
import { GROQ_API_KEY } from '../config/api-keys';

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
  joinAfter: string[];       // IDs of songs that should be joined (no pause) with the next song
  bisAfterSongId: string | null; // ID of the song after which to insert the BIS marker (null = no bis)
  explanation: string;
}

// Circle of fifths positions (0=C, 1=G, 2=D, ..., 11=F)
const NOTE_POS: Record<string, number> = {
  'C': 0, 'G': 1, 'D': 2, 'A': 3, 'E': 4, 'B': 5,
  'F#': 6, 'Gb': 6, 'C#': 7, 'Db': 7, 'G#': 8, 'Ab': 8,
  'D#': 9, 'Eb': 9, 'A#': 10, 'Bb': 10, 'F': 11,
};

// Extract root note from e.g. "Am", "F#m", "Bb", "E"
function rootNote(note: string): string | null {
  if (!note) return null;
  const m = note.match(/^([A-G][#b]?)/);
  return m ? m[1] : null;
}

// Distance on circle of fifths: 0 (unison) to 6 (tritone)
function fifthsDist(a: string, b: string): number | null {
  const pa = NOTE_POS[rootNote(a) ?? ''];
  const pb = NOTE_POS[rootNote(b) ?? ''];
  if (pa === undefined || pb === undefined) return null;
  const d = Math.abs(pa - pb);
  return Math.min(d, 12 - d);
}

// Quality label for distance
function transLabel(d: number | null): string {
  if (d === null) return '?';
  if (d === 0) return '★★★ misma tonalidad';
  if (d === 1) return '★★★ quinta perfecta';
  if (d === 2) return '★★☆ buena';
  if (d === 3) return '★☆☆ aceptable';
  if (d <= 5) return '☆☆☆ tensa';
  return '✗ tritono (evitar)';
}

@Injectable()
export class AiService {
  async generateSetlist(
    songs: SongInput[],
    preferences: string,
  ): Promise<SetlistResult> {
    const groqApiKey = GROQ_API_KEY;
    if (songs.length < 2) throw new BadRequestException('Se necesitan al menos 2 canciones');

    // Use short aliases (S1, S2...) instead of UUIDs so the LLM doesn't corrupt them
    const aliasToId = songs.map((s, i) => ({ alias: `S${i + 1}`, id: s.id }));
    const idToAlias = Object.fromEntries(aliasToId.map(({ alias, id }) => [id, alias]));

    const songList = songs.map((s, i) =>
      `S${i + 1}. "${s.title}" | BPM: ${s.tempo ?? '?'} | Estilo: ${s.style ?? '?'} | Nota inicio: ${s.startNote ?? '?'} | Nota fin: ${s.endNote ?? '?'} | Duración: ${s.duration ? Math.round(s.duration / 60) + 'min' : '?'}`
    ).join('\n');

    // Pre-compute pairwise transition scores
    const pairs = songs.flatMap((a) =>
      songs
        .filter(b => b.id !== a.id)
        .map(b => {
          const d = fifthsDist(a.endNote ?? '', b.startNote ?? '');
          return { fromAlias: idToAlias[a.id], toAlias: idToAlias[b.id], from: a, to: b, dist: d, label: transLabel(d) };
        })
    ).filter(p => p.dist !== null);

    const transitionTable = pairs.length > 0
      ? 'COMPATIBILIDAD DE TRANSICIONES (círculo de quintas — nota fin → nota inicio):\n' +
        'Distancia 0=misma tonalidad (ideal), 1=quinta perfecta (excelente), 2=buena, 3+=evitar si es posible\n' +
        pairs.map(p => `  ${p.fromAlias} → ${p.toAlias} ("${p.from.title}" → "${p.to.title}"): dist=${p.dist} ${p.label}`).join('\n')
      : '';

    const prompt = `Eres un experto en diseño de setlists para bandas de metal.
Tienes que ordenar las siguientes canciones para crear el mejor setlist posible.

CANCIONES (usa los identificadores S1, S2... en tu respuesta):
${songList}
${transitionTable ? '\n' + transitionTable : ''}

PREFERENCIAS DEL USUARIO: ${preferences || 'Sin preferencias específicas. Crea un setlist dinámico y equilibrado.'}

CRITERIOS PARA ORDENAR (en orden de prioridad):
1. Flujo de energía: arrancar fuerte, crear variedad dinámica, no poner lentas seguidas, acabar con impacto
2. Compatibilidad de tonalidades: usa la tabla de transiciones anterior — prioriza distancias 0 y 1 entre canciones adyacentes
3. Coherencia de tempo: evitar saltos bruscos de BPM (máx. 30 BPM de diferencia entre canciones adyacentes si es posible)
4. Si hay preferencias del usuario, priorizarlas sobre todo lo demás

ENCADENAR CANCIONES (joinAfter):
- Identifica pares o grupos de canciones que fluyan directamente sin pausa (distancia de tonalidad 0 o 1 Y BPM compatible).
- Devuelve en "joinAfter" el alias de cada canción que debe ir encadenada a la siguiente (sin pausa entre ellas).
- Solo encadena cuando sea musicalmente natural. No fuerces encadenamientos si el BPM difiere mucho.

BIS:
- Si el setlist tiene 4 o más canciones, decide cuál es la canción tras la que se hace el corte del BIS (el público pensará que ha terminado, y la banda vuelve para el bis).
- El bis debe ser el momento más intenso o emotivo. Suele ser entre las últimas 2 y 4 canciones.
- Devuelve en "bisAfterSongAlias" el alias de la canción justo ANTES del bis (es decir, la última canción del bloque principal).
- Si el setlist es muy corto (menos de 4 canciones) o no tiene sentido un bis, devuelve null.

RESPONDE ÚNICAMENTE con un objeto JSON válido con esta estructura exacta (sin texto adicional):
{
  "orderedAliases": ["S3", "S1", "S2", ...],
  "joinAfter": ["S2", "S5"],
  "bisAfterSongAlias": "S8",
  "explanation": "Explicación breve en español de por qué este orden funciona bien, qué transiciones de tonalidad se aprovecharon y qué canciones se encadenan (máx. 4 frases)"
}`;

    const response = await fetch('https://api.groq.com/openai/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${groqApiKey}`,
      },
      body: JSON.stringify({
        model: 'llama-3.3-70b-versatile',
        messages: [{ role: 'user', content: prompt }],
        temperature: 0.4,
        max_tokens: 1200,
        response_format: { type: 'json_object' },
      }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new BadRequestException(`Groq API error: ${error}`);
    }

    const data: any = await response.json();
    const content = data.choices?.[0]?.message?.content;
    if (!content) throw new BadRequestException('Respuesta vacía de la IA');

    try {
      const raw = JSON.parse(content) as any;
      const aliasMap = Object.fromEntries(aliasToId.map(({ alias, id }) => [alias, id]));

      const orderedAliases: string[] = Array.isArray(raw.orderedAliases) ? raw.orderedAliases : [];
      if (orderedAliases.length === 0) throw new Error('orderedAliases vacío');

      const orderedIds = orderedAliases
        .map((a: string) => aliasMap[a])
        .filter(Boolean);

      const joinAfterAliases: string[] = Array.isArray(raw.joinAfter) ? raw.joinAfter : [];
      const joinAfter = joinAfterAliases.map((a: string) => aliasMap[a]).filter(Boolean);

      const bisAlias: string | null = raw.bisAfterSongAlias ?? null;
      const bisAfterSongId = bisAlias ? (aliasMap[bisAlias] ?? null) : null;

      return {
        orderedIds,
        joinAfter,
        bisAfterSongId,
        explanation: raw.explanation ?? '',
      };
    } catch {
      throw new BadRequestException('La IA devolvió un formato inesperado');
    }
  }
}

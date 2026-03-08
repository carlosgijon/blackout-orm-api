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

@Injectable()
export class AiService {
  async generateSetlist(
    songs: SongInput[],
    preferences: string,
  ): Promise<SetlistResult> {
    const groqApiKey = GROQ_API_KEY;
    if (songs.length < 2) throw new BadRequestException('Se necesitan al menos 2 canciones');

    const songList = songs.map((s, i) =>
      `${i + 1}. ID="${s.id}" | "${s.title}" | BPM: ${s.tempo ?? '?'} | Estilo: ${s.style ?? '?'} | Nota inicio: ${s.startNote ?? '?'} | Nota fin: ${s.endNote ?? '?'} | Duración: ${s.duration ? Math.round(s.duration / 60) + 'min' : '?'}`
    ).join('\n');

    const prompt = `Eres un experto en diseño de setlists para bandas de metal.
Tienes que ordenar las siguientes canciones para crear el mejor setlist posible.

CANCIONES:
${songList}

PREFERENCIAS DEL USUARIO: ${preferences || 'Sin preferencias específicas. Crea un setlist dinámico y equilibrado.'}

CRITERIOS PARA ORDENAR:
- Compatibilidad de tonalidades (nota final de una → nota inicial de la siguiente): menor distancia en el círculo de quintas
- Flujo de energía: arrancar fuerte, crear variedad dinámica, no poner lentas seguidas
- Coherencia de tempo: evitar saltos bruscos de BPM (máx. 30 BPM de diferencia entre canciones adyacentes si es posible)
- Si hay preferencias del usuario, priorizarlas

ENCADENAR CANCIONES (joinAfter):
- Identifica pares o grupos de canciones que fluyan directamente sin pausa (misma tonalidad o muy cercana, BPM compatible).
- Devuelve en "joinAfter" el ID de cada canción que debe ir encadenada a la siguiente (sin pausa entre ellas).
- Solo encadena canciones cuando sea musicalmente natural. No fuerces encadenamientos.

BIS:
- Si el setlist tiene 4 o más canciones, decide cuál es la canción tras la que se hace el corte del BIS (el público pensará que ha terminado, y la banda vuelve para el bis).
- El bis debe ser el momento más intenso o emotivo. Suele ser entre las últimas 2 y 4 canciones.
- Devuelve en "bisAfterSongId" el ID de la canción justo ANTES del bis (es decir, la última canción del bloque principal).
- Si el setlist es muy corto (menos de 4 canciones) o no tiene sentido un bis, devuelve null.

RESPONDE ÚNICAMENTE con un objeto JSON válido con esta estructura exacta (sin texto adicional):
{
  "orderedIds": ["id1", "id2", "id3", ...],
  "joinAfter": ["id2", "id5"],
  "bisAfterSongId": "id8",
  "explanation": "Explicación breve en español de por qué este orden funciona bien y qué canciones se encadenan (máx. 4 frases)"
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
      const parsed = JSON.parse(content) as SetlistResult;
      if (!Array.isArray(parsed.orderedIds)) throw new Error('orderedIds inválido');
      parsed.joinAfter = Array.isArray(parsed.joinAfter) ? parsed.joinAfter : [];
      parsed.bisAfterSongId = parsed.bisAfterSongId ?? null;
      return parsed;
    } catch {
      throw new BadRequestException('La IA devolvió un formato inesperado');
    }
  }
}

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

RESPONDE ÚNICAMENTE con un objeto JSON válido con esta estructura exacta (sin texto adicional):
{
  "orderedIds": ["id1", "id2", "id3", ...],
  "explanation": "Explicación breve en español de por qué este orden funciona bien (máx. 3 frases)"
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
        max_tokens: 800,
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
      return parsed;
    } catch {
      throw new BadRequestException('La IA devolvió un formato inesperado');
    }
  }
}

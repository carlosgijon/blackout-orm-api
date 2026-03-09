"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AiService = void 0;
const common_1 = require("@nestjs/common");
const api_keys_1 = require("../config/api-keys");
const NOTE_POS = {
    'C': 0, 'G': 1, 'D': 2, 'A': 3, 'E': 4, 'B': 5,
    'F#': 6, 'Gb': 6, 'C#': 7, 'Db': 7, 'G#': 8, 'Ab': 8,
    'D#': 9, 'Eb': 9, 'A#': 10, 'Bb': 10, 'F': 11,
};
function rootNote(note) {
    if (!note)
        return null;
    const m = note.match(/^([A-G][#b]?)/);
    return m ? m[1] : null;
}
function fifthsDist(a, b) {
    const pa = NOTE_POS[rootNote(a) ?? ''];
    const pb = NOTE_POS[rootNote(b) ?? ''];
    if (pa === undefined || pb === undefined)
        return null;
    const d = Math.abs(pa - pb);
    return Math.min(d, 12 - d);
}
function transLabel(d) {
    if (d === null)
        return '?';
    if (d === 0)
        return '★★★ misma tonalidad';
    if (d === 1)
        return '★★★ quinta perfecta';
    if (d === 2)
        return '★★☆ buena';
    if (d === 3)
        return '★☆☆ aceptable';
    if (d <= 5)
        return '☆☆☆ tensa';
    return '✗ tritono (evitar)';
}
let AiService = class AiService {
    async generateSetlist(songs, preferences) {
        const groqApiKey = api_keys_1.GROQ_API_KEY;
        if (songs.length < 2)
            throw new common_1.BadRequestException('Se necesitan al menos 2 canciones');
        const aliasToId = songs.map((s, i) => ({ alias: `S${i + 1}`, id: s.id }));
        const idToAlias = Object.fromEntries(aliasToId.map(({ alias, id }) => [id, alias]));
        const songList = songs.map((s, i) => `S${i + 1}. "${s.title}" | BPM: ${s.tempo ?? '?'} | Estilo: ${s.style ?? '?'} | Nota inicio: ${s.startNote ?? '?'} | Nota fin: ${s.endNote ?? '?'} | Duración: ${s.duration ? Math.round(s.duration / 60) + 'min' : '?'}`).join('\n');
        const pairs = songs.flatMap((a) => songs
            .filter(b => b.id !== a.id)
            .map(b => {
            const d = fifthsDist(a.endNote ?? '', b.startNote ?? '');
            return { fromAlias: idToAlias[a.id], toAlias: idToAlias[b.id], from: a, to: b, dist: d, label: transLabel(d) };
        })).filter(p => p.dist !== null);
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
            throw new common_1.BadRequestException(`Groq API error: ${error}`);
        }
        const data = await response.json();
        const content = data.choices?.[0]?.message?.content;
        if (!content)
            throw new common_1.BadRequestException('Respuesta vacía de la IA');
        try {
            const raw = JSON.parse(content);
            const aliasMap = Object.fromEntries(aliasToId.map(({ alias, id }) => [alias, id]));
            const orderedAliases = Array.isArray(raw.orderedAliases) ? raw.orderedAliases : [];
            if (orderedAliases.length === 0)
                throw new Error('orderedAliases vacío');
            const orderedIds = orderedAliases
                .map((a) => aliasMap[a])
                .filter(Boolean);
            const joinAfterAliases = Array.isArray(raw.joinAfter) ? raw.joinAfter : [];
            const joinAfter = joinAfterAliases.map((a) => aliasMap[a]).filter(Boolean);
            const bisAlias = raw.bisAfterSongAlias ?? null;
            const bisAfterSongId = bisAlias ? (aliasMap[bisAlias] ?? null) : null;
            return {
                orderedIds,
                joinAfter,
                bisAfterSongId,
                explanation: raw.explanation ?? '',
            };
        }
        catch {
            throw new common_1.BadRequestException('La IA devolvió un formato inesperado');
        }
    }
};
exports.AiService = AiService;
exports.AiService = AiService = __decorate([
    (0, common_1.Injectable)()
], AiService);
//# sourceMappingURL=ai.service.js.map
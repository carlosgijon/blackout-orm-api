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
let AiService = class AiService {
    async generateSetlist(songs, preferences) {
        const groqApiKey = api_keys_1.GROQ_API_KEY;
        if (songs.length < 2)
            throw new common_1.BadRequestException('Se necesitan al menos 2 canciones');
        const songList = songs.map((s, i) => `${i + 1}. ID="${s.id}" | "${s.title}" | BPM: ${s.tempo ?? '?'} | Estilo: ${s.style ?? '?'} | Nota inicio: ${s.startNote ?? '?'} | Nota fin: ${s.endNote ?? '?'} | Duración: ${s.duration ? Math.round(s.duration / 60) + 'min' : '?'}`).join('\n');
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
            throw new common_1.BadRequestException(`Groq API error: ${error}`);
        }
        const data = await response.json();
        const content = data.choices?.[0]?.message?.content;
        if (!content)
            throw new common_1.BadRequestException('Respuesta vacía de la IA');
        try {
            const parsed = JSON.parse(content);
            if (!Array.isArray(parsed.orderedIds))
                throw new Error('orderedIds inválido');
            return parsed;
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
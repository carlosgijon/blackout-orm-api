package com.blackout.api.ai.application.service;

import com.blackout.api.ai.infrastructure.web.dto.GenerateSetlistRequest;
import com.blackout.api.ai.infrastructure.web.dto.SetlistResult;
import com.blackout.api.ai.infrastructure.web.dto.SongInput;
import com.blackout.api.shared.domain.BadRequestException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AiApplicationService {

    @Value("${groq.api-key:}")
    private String groqApiKey;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    // Circle of fifths positions
    private static final Map<String, Integer> NOTE_POS = Map.ofEntries(
            Map.entry("C", 0),
            Map.entry("G", 1),
            Map.entry("D", 2),
            Map.entry("A", 3),
            Map.entry("E", 4),
            Map.entry("B", 5),
            Map.entry("F#", 6),
            Map.entry("Gb", 6),
            Map.entry("C#", 7),
            Map.entry("Db", 7),
            Map.entry("G#", 8),
            Map.entry("Ab", 8),
            Map.entry("D#", 9),
            Map.entry("Eb", 9),
            Map.entry("A#", 10),
            Map.entry("Bb", 10),
            Map.entry("F", 11)
    );

    public AiApplicationService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public SetlistResult generateSetlist(List<SongInput> songs, String preferences) {
        if (songs.size() < 2) {
            throw new BadRequestException("At least 2 songs are required to generate a setlist");
        }

        // Build alias map S1..Sn
        Map<String, String> aliasToId = new LinkedHashMap<>();
        Map<String, String> idToAlias = new LinkedHashMap<>();
        for (int i = 0; i < songs.size(); i++) {
            String alias = "S" + (i + 1);
            aliasToId.put(alias, songs.get(i).id());
            idToAlias.put(songs.get(i).id(), alias);
        }

        // Build song list string
        StringBuilder songList = new StringBuilder();
        for (int i = 0; i < songs.size(); i++) {
            SongInput s = songs.get(i);
            String alias = "S" + (i + 1);
            int durationMin = s.duration() != null ? s.duration() / 60 : 0;
            int durationSec = s.duration() != null ? s.duration() % 60 : 0;
            songList.append(String.format(
                    "%s | %s - %s | BPM: %s | Estilo: %s | Tono inicio: %s | Tono fin: %s | Duración: %d:%02d%n",
                    alias,
                    s.title() != null ? s.title() : "?",
                    s.artist() != null ? s.artist() : "?",
                    s.tempo() != null ? s.tempo().toString() : "?",
                    s.style() != null ? s.style() : "?",
                    s.startNote() != null ? s.startNote() : "?",
                    s.endNote() != null ? s.endNote() : "?",
                    durationMin,
                    durationSec
            ));
        }

        // Pre-compute pairwise transitions
        StringBuilder transitions = new StringBuilder();
        for (int i = 0; i < songs.size(); i++) {
            for (int j = 0; j < songs.size(); j++) {
                if (i == j) continue;
                SongInput a = songs.get(i);
                SongInput b = songs.get(j);
                Integer dist = fifthsDist(a.endNote(), b.startNote());
                String label = transLabel(dist);
                transitions.append(String.format("S%d→S%d: %s%n", i + 1, j + 1, label));
            }
        }

        // Build prompt
        String prefsText = (preferences != null && !preferences.isBlank())
                ? "\nPreferencias adicionales: " + preferences
                : "";

        String prompt = String.format("""
                Eres un experto en producción de conciertos de metal. Organiza el siguiente setlist para máximo impacto.

                CANCIONES:
                %s
                TRANSICIONES (basado en círculo de quintas, tono final → tono inicial):
                %s
                INSTRUCCIONES:
                - Ordena TODAS las canciones usando sus alias (S1, S2, etc.)
                - Favorece transiciones ★★★ y ★★★
                - joinAfter: lista de alias de canciones que se encadenan sin pausa con la siguiente
                - bisAfterSongId: alias de la canción tras la cual iría un encore (opcional, null si no aplica)
                - explanation: 2-3 frases explicando la lógica del orden
                %s

                Responde ÚNICAMENTE con JSON válido en este formato exacto:
                {
                  "orderedIds": ["S1","S3","S2",...],
                  "joinAfter": ["S2"],
                  "bisAfterSongId": "S5",
                  "explanation": "..."
                }
                """,
                songList,
                transitions,
                prefsText
        );

        // Call Groq API
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", "llama-3.3-70b-versatile");
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            body.put("temperature", 0.4);
            body.put("max_tokens", 1200);
            body.put("response_format", Map.of("type", "json_object"));

            String responseBody = restClient.post()
                    .uri("https://api.groq.com/openai/v1/chat/completions")
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .body(String.class);

            Map<String, Object> response = objectMapper.readValue(responseBody, new TypeReference<>() {});

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new BadRequestException("No response from AI service");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            Map<String, Object> result = objectMapper.readValue(content, new TypeReference<>() {});

            // Map aliases back to IDs
            @SuppressWarnings("unchecked")
            List<String> orderedAliases = (List<String>) result.get("orderedIds");
            List<String> orderedIds = orderedAliases != null
                    ? orderedAliases.stream()
                        .map(alias -> aliasToId.getOrDefault(alias, alias))
                        .collect(Collectors.toList())
                    : Collections.emptyList();

            @SuppressWarnings("unchecked")
            List<String> joinAfterAliases = (List<String>) result.get("joinAfter");
            List<String> joinAfter = joinAfterAliases != null
                    ? joinAfterAliases.stream()
                        .map(alias -> aliasToId.getOrDefault(alias, alias))
                        .collect(Collectors.toList())
                    : Collections.emptyList();

            String bisAlias = (String) result.get("bisAfterSongId");
            String bisAfterSongId = bisAlias != null ? aliasToId.getOrDefault(bisAlias, bisAlias) : null;

            String explanation = (String) result.get("explanation");

            return new SetlistResult(orderedIds, joinAfter, bisAfterSongId, explanation);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("AI service error: " + e.getMessage());
        }
    }

    // --- Circle of fifths helpers ---

    private Integer rootNotePos(String note) {
        if (note == null || note.isBlank()) return null;
        Matcher m = Pattern.compile("^([A-G][#b]?)").matcher(note);
        if (!m.find()) return null;
        return NOTE_POS.get(m.group(1));
    }

    private Integer fifthsDist(String a, String b) {
        Integer pa = rootNotePos(a);
        Integer pb = rootNotePos(b);
        if (pa == null || pb == null) return null;
        int d = Math.abs(pa - pb);
        return Math.min(d, 12 - d);
    }

    private String transLabel(Integer d) {
        if (d == null) return "?";
        return switch (d) {
            case 0 -> "★★★ misma tonalidad";
            case 1 -> "★★★ quinta perfecta";
            case 2 -> "★★☆ buena";
            case 3 -> "★☆☆ aceptable";
            default -> d <= 5 ? "☆☆☆ tensa" : "✗ tritono (evitar)";
        };
    }
}

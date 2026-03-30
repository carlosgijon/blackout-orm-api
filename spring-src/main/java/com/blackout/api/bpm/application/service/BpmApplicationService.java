package com.blackout.api.bpm.application.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class BpmApplicationService {

    private final RestClient restClient;

    public BpmApplicationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Integer lookup(String title, String artist) {
        try {
            String query = URLEncoder.encode(title + " " + artist, StandardCharsets.UTF_8);

            Map<String, Object> searchData = restClient.get()
                    .uri("https://api.deezer.com/search?q={q}&limit=1", query)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (searchData == null) return null;

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> data = (List<Map<String, Object>>) searchData.get("data");
            if (data == null || data.isEmpty()) return null;

            Map<String, Object> first = data.get(0);

            // Check bpm in search result
            Object bpmObj = first.get("bpm");
            if (bpmObj instanceof Number bpmNum && bpmNum.doubleValue() > 0) {
                return bpmNum.intValue();
            }

            // Try fetching full track details
            Object idObj = first.get("id");
            if (idObj == null) return null;

            String trackId = idObj.toString();
            Map<String, Object> trackData = restClient.get()
                    .uri("https://api.deezer.com/track/{id}", trackId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (trackData == null) return null;

            Object trackBpm = trackData.get("bpm");
            if (trackBpm instanceof Number trackBpmNum && trackBpmNum.doubleValue() > 0) {
                return trackBpmNum.intValue();
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

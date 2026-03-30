package com.blackout.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the authentication endpoints.
 * Uses a real PostgreSQL container via Testcontainers.
 * The database starts empty — no pre-seeded users — so invalid-credentials
 * scenarios can be tested without fixtures.
 */
class AuthIntegrationTest extends AbstractIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /auth/login with unknown user returns 401")
    void login_unknownUser_returns401() throws Exception {
        Map<String, String> body = Map.of("username", "nobody", "password", "wrong");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/login with empty body returns 400")
    void login_emptyBody_returns400() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /actuator/health returns 200 (no auth required)")
    void healthEndpoint_isPublic() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/actuator/health"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /library without JWT returns 401")
    void protectedEndpoint_noJwt_returns401() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/library"))
            .andExpect(status().isUnauthorized());
    }
}

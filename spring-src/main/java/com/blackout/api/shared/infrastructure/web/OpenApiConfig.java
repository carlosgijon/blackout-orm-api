package com.blackout.api.shared.infrastructure.web;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Blackout Band Management API")
                .version("2.0.0 — Spring Boot")
                .description("Migración de NestJS a Spring Boot con arquitectura hexagonal"))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}

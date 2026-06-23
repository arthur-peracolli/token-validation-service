package com.challenge.token_validation_service.infrastructure.configs;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Contact;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Token Validation Service API")
                        .description("API para validação de tokens JWT conforme regras de negócio específicas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Arthur Peracolli")
                                .email("seu-email@email.com")
                                .url("https://github.com/arthur-peracolli"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local (Docker)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local (Maven)")
                ));
    }
}
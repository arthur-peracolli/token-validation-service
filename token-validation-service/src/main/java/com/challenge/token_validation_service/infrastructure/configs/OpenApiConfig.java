package com.challenge.token_validation_service.infrastructure.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Token Validation Service API")
                .description(
                    "API para validação de tokens JWT conforme regras de negócio específicas.")
                .version("1.0.0")
                .contact(
                    new Contact()
                        .name("Arthur Peracolli")
                        .email("arthurperacolli@hotmail.com")
                        .url("https://github.com/arthur-peracolli"))
                .license(
                    new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
  }
}

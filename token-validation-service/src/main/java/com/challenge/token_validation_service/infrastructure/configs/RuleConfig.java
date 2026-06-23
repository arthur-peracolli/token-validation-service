package com.challenge.token_validation_service.infrastructure.configs;


import com.challenge.token_validation_service.domain.rules.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RuleConfig {

    @Bean
    public List<ValidationRule> validationRules() {
        return List.of(
                new ClaimCountRule(),
                new NameRule(),
                new RoleRule(),
                new SeedRule()
        );
    }
}
package com.challenge.token_validation_service.infrastructure.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LambdaStartupConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Spring Application Context inicializado com sucesso para Lambda");
        System.out.println("Spring Application Context inicializado com sucesso para Lambda");
    }
}


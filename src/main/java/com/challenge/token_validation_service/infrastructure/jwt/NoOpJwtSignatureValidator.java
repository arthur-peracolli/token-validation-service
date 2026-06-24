package com.challenge.token_validation_service.infrastructure.jwt;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("challenge")
public class NoOpJwtSignatureValidator implements JwtSignatureValidator {

  @Override
  public boolean validate(String token) {
    return true;
  }
}

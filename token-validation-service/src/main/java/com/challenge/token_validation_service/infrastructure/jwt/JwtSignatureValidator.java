package com.challenge.token_validation_service.infrastructure.jwt;

public interface JwtSignatureValidator {

    boolean validate(String token);
}

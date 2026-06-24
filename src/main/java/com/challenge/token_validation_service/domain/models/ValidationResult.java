package com.challenge.token_validation_service.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ValidationResult {

  private final boolean valid;
  private final String errorMessage;

  public static ValidationResult success() {
    return ValidationResult.builder().valid(true).errorMessage(null).build();
  }

  public static ValidationResult failure(String errorMessage) {
    return ValidationResult.builder().valid(false).errorMessage(errorMessage).build();
  }
}

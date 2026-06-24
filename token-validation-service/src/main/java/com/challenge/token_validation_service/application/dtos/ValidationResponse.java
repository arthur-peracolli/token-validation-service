package com.challenge.token_validation_service.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationResponse {

  @JsonProperty("valid")
  private final boolean valid;
}

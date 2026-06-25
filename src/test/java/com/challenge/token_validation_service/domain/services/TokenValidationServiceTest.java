package com.challenge.token_validation_service.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.domain.rules.ValidationRule;
import com.challenge.token_validation_service.infrastructure.jwt.JwtPayloadExtractor;
import io.jsonwebtoken.JwtException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenValidationServiceTest {

  private final JwtPayloadExtractor extractor = mock(JwtPayloadExtractor.class);

  private final ValidationRule rule1 = mock(ValidationRule.class);

  private final ValidationRule rule2 = mock(ValidationRule.class);

  private final MeterRegistry meterRegistry = mock(MeterRegistry.class);

  private final Counter requestsCounter = mock(Counter.class);

  private final Counter successCounter = mock(Counter.class);

  private final Counter failureCounter = mock(Counter.class);

  private TokenValidationService service;

  @BeforeEach
  void setUp() {

    when(meterRegistry.counter("token.validation.requests")).thenReturn(requestsCounter);

    when(meterRegistry.counter("token.validation.success")).thenReturn(successCounter);

    when(meterRegistry.counter("token.validation.failure")).thenReturn(failureCounter);

    service = new TokenValidationService(extractor, List.of(rule1, rule2), meterRegistry);
  }

  @Test
  void shouldReturnTrueWhenAllRulesPass() {

    ClaimData claimData = ClaimData.builder().build();

    when(extractor.extract("token")).thenReturn(claimData);

    when(rule1.getOrder()).thenReturn(1);
    when(rule2.getOrder()).thenReturn(2);

    when(rule1.validate(claimData)).thenReturn(ValidationResult.success());
    when(rule2.validate(claimData)).thenReturn(ValidationResult.success());

    boolean result = service.validateToken("token");

    assertTrue(result);

    verify(rule1).validate(claimData);
    verify(rule2).validate(claimData);

    verify(requestsCounter).increment();
    verify(successCounter).increment();
    verify(failureCounter, never()).increment();
  }

  @Test
  void shouldReturnFalseWhenFirstRuleFails() {

    ClaimData claimData = ClaimData.builder().build();

    when(extractor.extract("token")).thenReturn(claimData);

    when(rule1.getOrder()).thenReturn(1);
    when(rule2.getOrder()).thenReturn(2);

    when(rule1.validate(claimData)).thenReturn(ValidationResult.failure("error"));

    boolean result = service.validateToken("token");

    assertFalse(result);

    verify(rule1).validate(claimData);
    verify(rule2, never()).validate(any());

    verify(requestsCounter).increment();
    verify(successCounter, never()).increment();
    verify(failureCounter).increment();
  }

  @Test
  void shouldReturnFalseWhenAnyRuleFails() {

    ClaimData claimData = ClaimData.builder().build();

    when(extractor.extract("token")).thenReturn(claimData);

    when(rule1.getOrder()).thenReturn(1);
    when(rule2.getOrder()).thenReturn(2);

    when(rule1.validate(claimData)).thenReturn(ValidationResult.success());

    when(rule2.validate(claimData)).thenReturn(ValidationResult.failure("error"));

    boolean result = service.validateToken("token");

    assertFalse(result);

    verify(requestsCounter).increment();
    verify(successCounter, never()).increment();
    verify(failureCounter).increment();
  }

  @Test
  void shouldExecuteRulesInOrder() {

    ClaimData claimData = ClaimData.builder().build();

    when(extractor.extract("token")).thenReturn(claimData);

    when(rule1.getOrder()).thenReturn(2);
    when(rule2.getOrder()).thenReturn(1);

    when(rule1.validate(claimData)).thenReturn(ValidationResult.success());
    when(rule2.validate(claimData)).thenReturn(ValidationResult.success());

    service.validateToken("token");

    var inOrder = inOrder(rule2, rule1);

    inOrder.verify(rule2).validate(claimData);
    inOrder.verify(rule1).validate(claimData);

    verify(requestsCounter).increment();
    verify(successCounter).increment();
    verify(failureCounter, never()).increment();
  }

  @Test
  void shouldReturnFalseWhenExtractorThrowsException() {

    when(extractor.extract("token")).thenThrow(new JwtException("invalid token"));

    boolean result = service.validateToken("token");

    assertFalse(result);

    verify(requestsCounter).increment();
    verify(successCounter, never()).increment();
    verify(failureCounter).increment();
  }

  @Test
  void shouldReturnFalseWhenUnexpectedExceptionOccurs() {

    ClaimData claimData = ClaimData.builder().build();

    when(extractor.extract("token")).thenReturn(claimData);

    when(rule1.getOrder()).thenReturn(1);

    when(rule1.validate(claimData)).thenThrow(new RuntimeException("unexpected"));

    boolean result = service.validateToken("token");

    assertFalse(result);

    verify(requestsCounter).increment();
    verify(successCounter, never()).increment();
    verify(failureCounter).increment();
  }
}

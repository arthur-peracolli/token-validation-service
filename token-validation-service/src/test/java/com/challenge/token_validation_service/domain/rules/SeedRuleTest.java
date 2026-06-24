package com.challenge.token_validation_service.domain.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import org.junit.jupiter.api.Test;

class SeedRuleTest {

  private final SeedRule rule = new SeedRule();

  @Test
  void shouldAcceptPrimeSeed() {

    ClaimData claim = ClaimData.builder().seed("17").build();

    assertTrue(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectNullSeed() {

    ClaimData claim = ClaimData.builder().seed(null).build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectEmptySeed() {

    ClaimData claim = ClaimData.builder().seed("").build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectNonPrimeSeed() {

    ClaimData claim = ClaimData.builder().seed("10").build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectSeedOne() {

    ClaimData claim = ClaimData.builder().seed("1").build();

    assertFalse(rule.validate(claim).isValid());
  }
}

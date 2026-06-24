package com.challenge.token_validation_service.domain.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import org.junit.jupiter.api.Test;

class ClaimCountRuleTest {

  private final ClaimCountRule rule = new ClaimCountRule();

  @Test
  void shouldAcceptExactlyThreeClaims() {

    ClaimData claim = ClaimData.builder().claimCount(3).build();

    assertTrue(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectLessThanThreeClaims() {

    ClaimData claim = ClaimData.builder().claimCount(2).build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectMoreThanThreeClaims() {

    ClaimData claim = ClaimData.builder().claimCount(4).build();

    assertFalse(rule.validate(claim).isValid());
  }
}

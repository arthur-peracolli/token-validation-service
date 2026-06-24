package com.challenge.token_validation_service.domain.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import org.junit.jupiter.api.Test;

class RoleRuleTest {

  private final RoleRule rule = new RoleRule();

  @Test
  void shouldAcceptAdminRole() {

    ClaimData claim = ClaimData.builder().role("Admin").build();

    assertTrue(rule.validate(claim).isValid());
  }

  @Test
  void shouldAcceptMemberRole() {

    ClaimData claim = ClaimData.builder().role("Member").build();

    assertTrue(rule.validate(claim).isValid());
  }

  @Test
  void shouldAcceptExternalRole() {

    ClaimData claim = ClaimData.builder().role("External").build();

    assertTrue(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectNullRole() {

    ClaimData claim = ClaimData.builder().role(null).build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectInvalidRole() {

    ClaimData claim = ClaimData.builder().role("CEO").build();

    assertFalse(rule.validate(claim).isValid());
  }
}

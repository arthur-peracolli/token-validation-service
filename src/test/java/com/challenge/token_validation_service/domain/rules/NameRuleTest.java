package com.challenge.token_validation_service.domain.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import org.junit.jupiter.api.Test;

class NameRuleTest {

  private final NameRule rule = new NameRule();

  @Test
  void shouldAcceptValidName() {

    ClaimData claim = ClaimData.builder().name("Maria Olivia").build();

    assertTrue(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectNullName() {

    ClaimData claim = ClaimData.builder().name(null).build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectBlankName() {

    ClaimData claim = ClaimData.builder().name(" ").build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectNameContainingNumbers() {

    ClaimData claim = ClaimData.builder().name("M4ria Olivia").build();

    assertFalse(rule.validate(claim).isValid());
  }

  @Test
  void shouldRejectNameLongerThan256Characters() {

    String name = "A".repeat(257);

    ClaimData claim = ClaimData.builder().name(name).build();

    assertFalse(rule.validate(claim).isValid());
  }
}

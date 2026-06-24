package com.challenge.token_validation_service.architecture;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.domain.rules.*;
import org.junit.jupiter.api.Test;

class ValidationRuleOrderTest {

  @Test
  void rulesShouldHaveUniqueOrder() {

    assertEquals(1, new ClaimCountRule().getOrder());
    assertEquals(2, new NameRule().getOrder());
    assertEquals(3, new RoleRule().getOrder());
    assertEquals(4, new SeedRule().getOrder());
  }
}

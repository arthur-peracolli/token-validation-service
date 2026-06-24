package com.challenge.token_validation_service.domain.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrimeNumberValidatorTest {

  @Test
  void shouldReturnFalseForNegativeNumbers() {

    assertFalse(PrimeNumberValidator.isPrime(-7));
  }

  @Test
  void shouldReturnFalseForZero() {

    assertFalse(PrimeNumberValidator.isPrime(0));
  }

  @Test
  void shouldReturnFalseForOne() {

    assertFalse(PrimeNumberValidator.isPrime(1));
  }

  @Test
  void shouldReturnTrueForTwo() {

    assertTrue(PrimeNumberValidator.isPrime(2));
  }

  @Test
  void shouldReturnTrueForPrimeOddNumber() {

    assertTrue(PrimeNumberValidator.isPrime(17));
  }

  @Test
  void shouldReturnTrueForLargePrimeNumber() {

    assertTrue(PrimeNumberValidator.isPrime(997));
  }

  @Test
  void shouldReturnFalseForEvenNumberGreaterThanTwo() {

    assertFalse(PrimeNumberValidator.isPrime(10));
  }

  @Test
  void shouldReturnFalseForOddCompositeNumber() {

    assertFalse(PrimeNumberValidator.isPrime(21));
  }

  @Test
  void shouldReturnTrueForValidPrimeString() {

    assertTrue(PrimeNumberValidator.isPrime("17"));
  }

  @Test
  void shouldReturnFalseForValidNonPrimeString() {

    assertFalse(PrimeNumberValidator.isPrime("10"));
  }

  @Test
  void shouldReturnFalseForInvalidString() {

    assertFalse(PrimeNumberValidator.isPrime("abc"));
  }

  @Test
  void shouldReturnFalseForNullString() {

    assertFalse(PrimeNumberValidator.isPrime((String) null));
  }

  @Test
  void shouldReturnFalseForEmptyString() {

    assertFalse(PrimeNumberValidator.isPrime(""));
  }
}

package com.challenge.token_validation_service.infrastructure;

import static com.challenge.token_validation_service.shared.constants.ErrorMessages.JWT_INVALID_STRUCTURE;
import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.infrastructure.jwt.JwtPayloadExtractor;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class JwtPayloadExtractorTest {

  private JwtPayloadExtractor extractor;

  @BeforeEach
  void setup() {
    extractor = new JwtPayloadExtractor(new ObjectMapper());
  }

  @Test
  void shouldExtractValidClaims() {

    String token =
        "eyJhbGciOiJIUzI1NiJ9."
            + "eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9."
            + "signature";

    ClaimData result = extractor.extract(token);

    assertEquals("Toninho Araujo", result.getName());

    assertEquals("Admin", result.getRole());

    assertEquals("7841", result.getSeed());

    assertEquals(3, result.getClaimCount());
  }

  @Test
  void shouldExtractClaimsWhenAdditionalClaimsExist() {

    String token =
        "header."
            + "eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIiwiRXh0cmEiOiJ4In0."
            + "signature";

    ClaimData result = extractor.extract(token);

    assertEquals(4, result.getClaimCount());
  }

  @Test
  void shouldThrowExceptionWhenTokenHasLessThanThreeParts() {

    JwtException exception =
        assertThrows(JwtException.class, () -> extractor.extract("header.payload"));

    assertEquals(JWT_INVALID_STRUCTURE, exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenTokenHasMoreThanThreeParts() {

    JwtException exception = assertThrows(JwtException.class, () -> extractor.extract("a.b.c.d"));

    assertEquals(JWT_INVALID_STRUCTURE, exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenPayloadIsNotBase64() {

    assertThrows(Exception.class, () -> extractor.extract("header.invalid-payload.signature"));
  }

  @Test
  void shouldReturnNullWhenClaimDoesNotExist() {

    String token = "header." + "eyJSb2xlIjoiQWRtaW4ifQ." + "signature";

    ClaimData result = extractor.extract(token);

    assertNull(result.getName());

    assertNull(result.getSeed());

    assertEquals("Admin", result.getRole());

    assertEquals(1, result.getClaimCount());
  }
}

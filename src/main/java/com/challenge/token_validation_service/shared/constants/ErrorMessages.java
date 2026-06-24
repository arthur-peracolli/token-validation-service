package com.challenge.token_validation_service.shared.constants;

public final class ErrorMessages {

  private ErrorMessages() {}

  public static final String CLAIM_COUNT_INVALID = "JWT must contain exactly 3 claims";

  public static final String NAME_EMPTY = "Name is empty";

  public static final String NAME_TOO_LONG = "Name exceeds maximum length of 256 characters";

  public static final String NAME_CONTAINS_NUMBER = "Name contains numeric characters";

  public static final String ROLE_EMPTY = "Role is empty";

  public static final String ROLE_INVALID = "Role is invalid";

  public static final String SEED_EMPTY = "Seed is empty";

  public static final String SEED_NOT_PRIME = "Seed is not a prime number";

  public static final String JWT_INVALID_STRUCTURE = "Invalid JWT structure";

  public static final String JWT_INVALID_PAYLOAD = "Invalid JWT payload";
}

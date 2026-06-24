package com.challenge.token_validation_service.shared.constants;

public final class Roles {

  private Roles() {}

  public static final String ADMIN = "Admin";
  public static final String MEMBER = "Member";
  public static final String EXTERNAL = "External";

  public static final String[] VALID_ROLES = {ADMIN, MEMBER, EXTERNAL};
}

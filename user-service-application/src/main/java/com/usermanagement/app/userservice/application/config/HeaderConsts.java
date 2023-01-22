package com.usermanagement.app.userservice.application.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderConsts {

  public static final String HEADER_USER_ID = "x-user-id";
  public static final String HEADER_EMAIL = "x-email";
  public static final String HEADER_EMAIL_VERIFIED = "x-email-verified";
  public static final String HEADER_USER_ROLE = "x-scope";
}

package com.usermanagement.app.userservice.application.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for the User Uri Path.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UriPathConsts {

  /**
   * Main Uri.
   */
  public static final String MAIN = "/v1/users";

  /**
   * Current User Uri path.
   */

  public static final String CURRENT_USER = MAIN + "/current";
  public static final String CREATE_USER = CURRENT_USER;
  public static final String UPDATE_USER = CURRENT_USER;
  public static final String DELETE_USER = CURRENT_USER;

  public static final String DEPOSIT_USER = CURRENT_USER + "/deposit";
  public static final String DEPOSIT_RESET_USER = CURRENT_USER + "/reset";

  /**
   * Swagger Path
   */
  public static final String SWAGGER_PATH = "/swagger-ui/api-docs.html";

  /**
   * Admin Path
   */
  public static final String ADMIN = "/admin";

  /**
   * User admin path
   */
  public static final String ADMIN_USER = ADMIN + MAIN;

  public static final String ADMIN_SET_ROLE = ADMIN_USER + "/role";

  public static final String ADMIN_DELETE_USER = ADMIN_USER;

  /**
   * Internal constants for the User Uri Path.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class InternalUriPathConsts {
    public static final String INTERNAL = "/internal";

  }

}

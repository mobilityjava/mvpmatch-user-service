package com.usermanagement.app.userservice.domain.config;

public enum AuthorizationEnum {
  //Normal users with Buyer role
  USER_ROLE("user"),
  ADMIN_ROLE("admin"),
  SELLER_ROLE("seller"),
  ROLES_CLAIM("scope");

  private String authorizationRole;

  AuthorizationEnum(String authorizationRole) { this.authorizationRole = authorizationRole; }

  public String getAuthRole() { return authorizationRole; }
}


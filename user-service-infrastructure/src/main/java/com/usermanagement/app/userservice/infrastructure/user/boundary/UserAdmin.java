package com.usermanagement.app.userservice.infrastructure.user.boundary;

import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;

/**
 * Provides all user administration functions that are implemented by an identity provider
 */
public interface UserAdmin {

  /**
   * Add the {@link AuthorizationEnum} role to user's {@link
   * AuthorizationEnum#ROLES_CLAIM} claim
   *
   * @param userId user to set a role on firebase token
   */
  void setUserRole(String userId, AuthorizationEnum role);


  /**
   * Deletes a firebase user.
   * @param userId User to delete.
   */
  void deleteFirebaseUser(String userId);
}

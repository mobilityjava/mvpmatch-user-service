package com.usermanagement.app.userservice.domain.user;

import com.usermanagement.app.userservice.domain.common.PagingQuery;
import com.usermanagement.app.userservice.domain.common.PagingResult;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import java.util.Optional;
import lombok.NonNull;

/**
 * All domain actions regarding an {@link User}.
 */
public interface UserService {

  /**
   * Get Current User
   *
   * @param userId
   * @return current user.
   */
  Optional<User> getUser(String userId);

  /**
   * Create a User
   *
   * @param user object. Cannot be null.
   * @return Return created User value.
   */
  Optional<User> createUser(@NonNull User user);

  /**
   * Update a user
   *
   * @param user object. Cannot be null.
   * @return Return updated User value.
   */
  Optional<User> updateUser(@NonNull User user);

  /**
   * Deletes a user's account.
   * @param userId Cannot be {@code null}.
   * @param deletedByWhom Cannot be {@code null}.
   */
  void deleteUser(String userId, String deletedByWhom);

  /**
   * Deletes a user's account from firebase in case there is no profile existed.
   * @param userId Cannot be {@code null}.
   */
  void deleteFirebaseUser(String userId);

  /**
   * Get all available users sorted by e-mail.
   *
   * @param pagingQuery paging details
   * @return requested page of users
   */
  PagingResult<User> getUsers(PagingQuery pagingQuery, String userQuery);

  /**
   * Marks user with specific role.
   *
   * @param userId of user
   */
  boolean setUserRole(String userId, AuthorizationEnum role);

  /**
   * Check if user with id already exists
   * @param userId
   * @return true if user with id already exists
   */
  boolean exists(String userId);

}

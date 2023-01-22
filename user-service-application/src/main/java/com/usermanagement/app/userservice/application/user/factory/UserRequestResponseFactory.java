package com.usermanagement.app.userservice.application.user.factory;

import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import com.usermanagement.app.userservice.domain.user.User;
import java.time.OffsetDateTime;

public interface UserRequestResponseFactory {

  /**
   * Creates a User from an UserRequest.
   *
   * @param userRequest of {@link UserRequest} type.
   * @return User.
   */
  User toUserUpdate(UserRequest userRequest,
      String userId,
      String email,
      User user);

  /**
   * Creates a User for deposit change.
   *
   * @param validValue of {@link Integer} type.
   * @return User.
   */
  User toUserWithDeposit(Integer validValue,
      String userId,
      User user);

  /**
   * Creates a User from an UserRequest.
   *
   * @param userRequest of {@link UserRequest} type.
   * @return User.
   */
  User toUser(UserRequest userRequest,
      String userId,
      String email,
      OffsetDateTime generalTerms,
      OffsetDateTime dataProtection);

  /**
   * Creates a UserResponse from an User.
   *
   * @param user of {@link User} type.
   * @return User.
   */
  UserResponse toUserResponse(User user);

}

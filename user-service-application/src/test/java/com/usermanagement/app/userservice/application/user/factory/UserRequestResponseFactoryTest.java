package com.usermanagement.app.userservice.application.user.factory;

import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import com.usermanagement.app.userservice.application.support.UserMock;
import com.usermanagement.app.userservice.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserRequestResponseFactoryTest {

  private UserRequestResponseFactory userRequestResponseFactory;

  @BeforeEach
  void setUp() {
    this.userRequestResponseFactory = new UserRequestResponseFactoryImpl();
  }


  @DisplayName("should map UserRequest to User")
  @Test
  void shouldMapUserRequestToUser() {

    User userRequestType = UserMock.getInstance().getUser2();
    UserRequest userStatusRequestType = UserMock.getInstance().getUserRequest();
    OffsetDateTime sampleOffsetDateTime = UserMock.getInstance().getOffsetDateTime();
    String sampleUserId = "userId2";
    String sampleEmail = "some.mail@no.op";

    User actual = this.userRequestResponseFactory.toUser(
        userStatusRequestType,
        sampleUserId,
        sampleEmail,
        sampleOffsetDateTime,
        sampleOffsetDateTime);

    assertNotNull(actual);
    assertEquals(userRequestType, actual);
  }

  @DisplayName("should map UserRequest to User for update operation")
  @Test
  void shouldMapUserRequestToUserForUpdateOperation() {
    User userType = UserMock.getInstance().getUser2();
    UserRequest userRequestType = UserMock.getInstance().getUserRequest();
    String sampleUserId = "userId2";
    String sampleEmail = "some.mail@no.op";

    User actual = this.userRequestResponseFactory.toUserUpdate(
        userRequestType,
        sampleUserId,
        sampleEmail,
        userType);

    assertNotNull(actual);
    assertEquals(userType, actual);

  }

  @DisplayName("should map User to UserResponse")
  @Test
  void shouldMapUserToUserResponse() {

    User userResponseType = UserMock.getInstance().getUser1();
    UserResponse userStatusResponseType = UserMock.getInstance().getUserResponse();

    UserResponse actual = this.userRequestResponseFactory.toUserResponse(userResponseType);

    assertNotNull(actual);
    assertEquals(userStatusResponseType, actual);
  }

  @DisplayName("should map UserRequest's Email in LowerCase and Trim to User")
  @Test
  void shouldMapUserEmailInLowercaseAndTrimToUserDTO() {

    User userRequestType = UserMock.getInstance().getUser1();
    UserRequest userStatusRequestType = UserMock.getInstance().getUserRequest();
    OffsetDateTime sampleOffsetDateTime = UserMock.getInstance().getOffsetDateTime();
    String sampleUserId = UserMock.getInstance().getUserId();
    String sampleEmail = UserMock.getInstance().getDefaultEmail();

    User actual = this.userRequestResponseFactory.toUser(
        userStatusRequestType,
        sampleUserId,
        sampleEmail,
        sampleOffsetDateTime,
        sampleOffsetDateTime);

    assertNotNull(actual);
    assertEquals(userRequestType.getEmail(), actual.getEmail());

  }

}

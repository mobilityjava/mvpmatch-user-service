package com.usermanagement.app.userservice.infrastructure.user.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuthException;
import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.infrastructure.user.factory.UserFactory;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserRepository;
import com.usermanagement.app.userservice.infrastructure.user.support.UserMock;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  public static final String EXISTING_MAIL = "email";

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserFactory userFactory;

  @Mock
  private UserAdmin userAdmin;

  private UserServiceImpl userService;

  @BeforeEach
  void setup() {
    userService = new UserServiceImpl(userRepository, userFactory, userAdmin);
    lenient().when(userRepository.existsByEmailAndDeletedContains(EXISTING_MAIL,
            UserDeletionStatus.ACTIVE.toString()))
        .thenReturn(Boolean.TRUE);
  }


  @Test
  void getUserByUserIdShouldReturnUser() {

    String userId = "userId";

    Optional<UserDocument> userDTO = userRepository.findByUserIdAndDeletedContains(userId, UserDeletionStatus.ACTIVE.toString());
    Optional<User> expected = userDTO.map(userFactory::toUser);

    Optional<User> actual = userService.getUser(userId);

    assertEquals(actual, expected);

  }

  @Test
  void deleteUserShouldSucceed() throws FirebaseAuthException {

    final var userDTO = UserMock.getInstance().getUserDTO();
    when(userRepository.findByUserIdAndDeletedContains("userId", UserDeletionStatus.ACTIVE.toString())).thenReturn(
        Optional.ofNullable(userDTO));

    userService.deleteUser("userId", UserDeletionStatus.USER.toString());

    verify(userAdmin, times(1)).deleteFirebaseUser("userId");
    verifyNoMoreInteractions(userAdmin);

  }

  @Test
  void deleteUserShouldThrowIllegalArgumentExceptionIfUserNotFound() {

    when(userRepository.findByUserIdAndDeletedContains(anyString(), anyString())).thenReturn(
        Optional.empty());
    assertThrows(IllegalArgumentException.class, () ->  userService.deleteUser("userId", UserDeletionStatus.USER.toString()));

  }

}

package com.usermanagement.app.userservice.infrastructure.user.firebase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirebaseUserAdminTest {

  private FirebaseUserAdmin userAdmin;

  @Mock
  private FirebaseAuthDelegate firebaseAuth;

  @BeforeEach
  void setup() {
    userAdmin = new FirebaseUserAdmin(firebaseAuth);
  }


  @Test
  void setUserRoleShouldAddAdminRoleClaimToAnUser() throws FirebaseAuthException {
    final var mock = Mockito.mock(UserRecord.class);
    when(mock.getCustomClaims()).thenReturn(
        Map.of(AuthorizationEnum.ROLES_CLAIM.getAuthRole(), Set.of(AuthorizationEnum.USER_ROLE.getAuthRole())));
    when(firebaseAuth.getUser(anyString())).thenReturn(mock);

    userAdmin.setUserRole("userId", AuthorizationEnum.ADMIN_ROLE);

    ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
    verify(firebaseAuth).setCustomUserClaims(eq("userId"), captor.capture());
    verifyNoMoreInteractions(firebaseAuth);

    final var claims = captor.getValue();
    assertEquals(1, claims.size());
    assertNotNull(claims.get(AuthorizationEnum.ROLES_CLAIM.getAuthRole()));
    assertTrue(claims.get(AuthorizationEnum.ROLES_CLAIM.getAuthRole()) instanceof Collection);
    final var roles = (Collection<String>) claims.get(AuthorizationEnum.ROLES_CLAIM.getAuthRole());
    assertTrue(roles.contains(AuthorizationEnum.USER_ROLE.getAuthRole()));
    assertTrue(roles.contains(AuthorizationEnum.ADMIN_ROLE.getAuthRole()));
  }

  @Test
  void deleteUserShouldSucceed() throws FirebaseAuthException {
    userAdmin.deleteFirebaseUser("userId");

    verify(firebaseAuth, times(1)).deleteUser("userId");
    verifyNoMoreInteractions(firebaseAuth);
  }

}

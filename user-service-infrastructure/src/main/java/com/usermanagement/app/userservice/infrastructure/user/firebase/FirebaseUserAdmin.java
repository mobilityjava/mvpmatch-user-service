package com.usermanagement.app.userservice.infrastructure.user.firebase;

import com.google.firebase.auth.FirebaseAuthException;
import com.usermanagement.app.userservice.infrastructure.user.boundary.UserAdmin;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
@ConditionalOnProperty(prefix = "usermanagement.app.firebase", value = "enabled")
public class FirebaseUserAdmin implements UserAdmin {

  private final FirebaseAuthDelegate firebaseAuth;

  @Override
  public void setUserRole(String userId, AuthorizationEnum role) {
    try {
      setRoles(userId, role.getAuthRole());
      log.info("User {} is now {}", userId, role.getAuthRole());
    } catch (FirebaseAuthException e) {
      throw new IllegalStateException(
          String.format("User '%s' could not be marked as '%s'.",
              userId, role.getAuthRole()), e);
    }
  }

  @Override
  public void deleteFirebaseUser(String userId) {
    try {
      firebaseAuth.deleteUser(userId);
      log.info("User {} has been deleted.", userId);
    } catch (FirebaseAuthException e) {
      throw new IllegalStateException(String.format("User '%s' could not be deleted.", userId),
          e);
    }
  }

  private void setRoles(String userId, String... userRoles) throws FirebaseAuthException {

    Map<String, Object> claims = new HashMap<>(firebaseAuth.getUser(userId).getCustomClaims());
    Set<String> newRoles = new HashSet<>(Set.of(userRoles));
    newRoles.addAll(extractOldRoles(claims));
    claims.put(AuthorizationEnum.ROLES_CLAIM.getAuthRole(), newRoles);
    firebaseAuth.setCustomUserClaims(userId, claims);
  }

  private Set<String> extractOldRoles(Map<String, Object> claims) {
    if (claims == null || claims.isEmpty()) {
      return Set.of();
    }
    final var oldRoles = claims.get(AuthorizationEnum.ROLES_CLAIM.getAuthRole());
    if (existingRolesAsStringCollection(oldRoles)) {
      return Set.copyOf((Collection<? extends String>) oldRoles);
    }
    return Set.of();
  }

  private boolean existingRolesAsStringCollection(Object oldRoles) {
    return oldRoles instanceof Collection && !((Collection<?>) oldRoles).isEmpty()
        && ((Collection<?>) oldRoles).iterator().next() instanceof String;
  }
}

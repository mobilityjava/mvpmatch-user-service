package com.usermanagement.app.userservice.infrastructure.user.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "usermanagement.app.firebase", value = "enabled")
public class FirebaseAuthDelegate {

  private final FirebaseAuth firebaseAuth;

  public void deleteUser(String userId) throws FirebaseAuthException {
    firebaseAuth.deleteUser(userId);
  }

  public UserRecord getUser(String userId) throws FirebaseAuthException {
    return firebaseAuth.getUser(userId);
  }

  public void setCustomUserClaims(String userId, Map<String, Object> claims)
      throws FirebaseAuthException {
    firebaseAuth.setCustomUserClaims(userId,claims);
  }
}

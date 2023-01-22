package com.usermanagement.app.userservice.infrastructure.user.firebase;

import com.usermanagement.app.userservice.infrastructure.user.boundary.UserAdmin;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "usermanagement.app.firebase", value = "enabled", matchIfMissing = true, havingValue = "false")
@Component
@Slf4j
public class NoopUserAdmin implements UserAdmin {

  @Override
  public void setUserRole(String userId, AuthorizationEnum role) {
    log.warn("Noop class active. 'setUserRole' will not be executed on Firebase");
  }

  @Override
  public void deleteFirebaseUser(String userId) {
    log.warn("Noop class active. 'Delete Firebase User' will not be executed on Firebase");
  }
}

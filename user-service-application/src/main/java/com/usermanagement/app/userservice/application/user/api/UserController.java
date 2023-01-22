package com.usermanagement.app.userservice.application.user.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.usermanagement.app.userservice.application.config.HeaderConsts;
import com.usermanagement.app.userservice.application.config.UriPathConsts;
import com.usermanagement.app.userservice.application.support.HeaderTools;
import com.usermanagement.app.userservice.application.user.api.doc.UserControllerDoc;
import com.usermanagement.app.userservice.application.user.factory.UserRequestResponseFactory;
import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.domain.user.UserService;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

  private final UserService userService;
  private final UserRequestResponseFactory userFactory;

  @GetMapping(path = UriPathConsts.CURRENT_USER, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserResponse currentUser(@RequestHeader Map<String, String> headers) {

    String userId = headers.getOrDefault(HeaderConsts.HEADER_USER_ID, "");

    var user = userService.getUser(userId);
    if (user.isEmpty()) {
      throw new ResponseStatusException(NOT_FOUND, "User does not have profile yet");
    }
    return userFactory.toUserResponse(user.get());
  }

  @PostMapping(path = UriPathConsts.CREATE_USER)
  public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request,
      @RequestHeader Map<String, String> headers) {
    String userId = extractUserId(headers);

    Optional<String> emailJwtOpt = extractEmail(headers);
    final String email = extractEmail(request, emailJwtOpt);
    boolean emailVerified = extractEmailVerified(headers);
    userVerification(email);
    userEmailVerification(emailVerified);
    if (userService.exists(userId)) {
      throw new ResponseStatusException(CONFLICT,
          String.format("A user with the id '%s' already exists.", userId));
    }

    var currentUserStatusResponse = userService.createUser(
            userFactory.toUser(request, userId, email,
                OffsetDateTime.now().withNano(0), OffsetDateTime.now().withNano(0)))
        .orElseThrow(
            () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "User can not be created"));

    UserResponse result = userFactory.toUserResponse(currentUserStatusResponse);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PutMapping(path = UriPathConsts.UPDATE_USER)
  public ResponseEntity<UserResponse> updateUser(
      @RequestBody UserRequest request,
      @RequestHeader Map<String, String> headers) {
    String userId = extractUserId(headers);
    // update email will be handled later
    String email = extractEmail(headers).orElseThrow(
        () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "User's Email must not be null."));
    boolean emailVerified = extractEmailVerified(headers);
    userVerification(email);
    userEmailVerification(emailVerified);

    User currentUser = userService.getUser(userId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
            String.format("There exists no user with the ID '%s'.", userId)));

    final var user = userFactory.toUserUpdate(request, userId, email, currentUser);

    var currentUserStatusResponse = userService
        .updateUser(user)
        .orElseThrow(
            () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "User could not be updated."));

    UserResponse result = userFactory.toUserResponse(currentUserStatusResponse);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PutMapping(path = UriPathConsts.DEPOSIT_USER)
  public ResponseEntity<UserResponse> deposit(@RequestBody String depositValue, @RequestHeader Map<String, String> headers) {
    AuthorizationEnum userRole = extractUserRole(headers);
    var kkk= extractEmail(headers);

    if(userRole != AuthorizationEnum.USER_ROLE) {
      throw new ResponseStatusException(BAD_REQUEST, "User should have BUYER role to be able to deposit");
    }
    String userId = extractUserId(headers);

    Integer validValue = validateDeposit(depositValue);

    User currentUser = userService.getUser(userId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
            String.format("There exists no user with the ID '%s'.", userId)));

    final var user = userFactory.toUserWithDeposit(validValue, userId, currentUser);

    var userStatusResponse = userService
        .updateUser(user)
        .orElseThrow(
            () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Value could not be deposited."));

    UserResponse result = userFactory.toUserResponse(userStatusResponse);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PutMapping(path = UriPathConsts.DEPOSIT_RESET_USER)
  public ResponseEntity<UserResponse> depositReset(@RequestHeader Map<String, String> headers) {
    AuthorizationEnum userRole = extractUserRole(headers);
    if(userRole != AuthorizationEnum.USER_ROLE) {
      throw new ResponseStatusException(BAD_REQUEST, "User should have BUYER role to be able to deposit");
    }
    String userId = extractUserId(headers);

    User currentUser = userService.getUser(userId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
            String.format("There exists no user with the ID '%s'.", userId)));

    final var user = userFactory.toUserWithDeposit(0, userId, currentUser);

    var userStatusResponse = userService
        .updateUser(user)
        .orElseThrow(
            () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Value could not be deposited."));

    UserResponse result = userFactory.toUserResponse(userStatusResponse);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping(path = UriPathConsts.DELETE_USER)
  public ResponseEntity<Void> deleteUser(@RequestHeader Map<String, String> headers) {
    String userId = extractUserId(headers);
    if (userService.getUser(userId).isEmpty()) {
      userService.deleteFirebaseUser(userId);
    } else {
      userService.deleteUser(userId, UserDeletionStatus.USER.toString());
    }
    return ResponseEntity.ok().build();
  }

  //  We always take userId, Email, emailVerified from tokenHeader
  private String extractUserId(Map<String, String> headers) {
    return HeaderTools.extractUserId(headers).orElseThrow(
        () -> new ResponseStatusException(NOT_FOUND, "User ID must not be null."));
  }

  private AuthorizationEnum extractUserRole(Map<String, String> headers) {
    return HeaderTools.getUserRole(HeaderTools.extractUserRole(headers).orElse(""));
  }

  private Optional<String> extractEmail(Map<String, String> headers) {
    return HeaderTools.extractEmail(headers);
  }

  private Boolean extractEmailVerified(Map<String, String> headers) {
    return Boolean.parseBoolean(HeaderTools.extractEmailVerified(headers).orElseThrow(
        () -> new ResponseStatusException(INTERNAL_SERVER_ERROR,
            "Email Verified must not be null.")));
  }

  private Boolean userVerification(String email) {
    if (!StringUtils.isNotBlank(email)) {
      throw new ResponseStatusException(BAD_REQUEST, "User E-mail could not be blank");
    }
    return true;
  }

  private Integer validateDeposit(String value) {
    if (!StringUtils.isNotBlank(value)) {
      throw new ResponseStatusException(BAD_REQUEST, "Deposit value could not be blank");
    }
    if (!validateValue(Integer.parseInt(value))) {
      throw new ResponseStatusException(BAD_REQUEST, "Deposit value should be either 5, 10, 20, 50, or 100");
    }
    return Integer.parseInt(value);
  }

  private boolean validateValue(Integer value) {
    Set<Integer> setValue = new HashSet<>();
    setValue.add(5);
    setValue.add(10);
    setValue.add(20);
    setValue.add(50);
    setValue.add(100);
    return setValue.contains(value);
  }

  private Boolean userEmailVerification(boolean emailVerified) {
    if (!emailVerified) {
      throw new ResponseStatusException(BAD_REQUEST, "User E-mail should be verified");
    }
    return true;
  }

  private String extractEmail(UserRequest request, Optional<String> emailJwtOpt) {
    if (emailJwtOpt.isPresent() && request.getEmail() != null) {
      // verify emails match
      return emailJwtOpt.filter(emailFromJwt -> request.getEmail().equals(emailFromJwt))
          .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,
              "E-Mail from body and header did not match"));
    } else if (emailJwtOpt.isPresent()) {
      return emailJwtOpt.get();
    } else if (request.getEmail() != null) {
      return request.getEmail();
    } else {
      throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "User's Email must not be null.");
    }
  }

}

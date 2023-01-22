package com.usermanagement.app.userservice.application.user.api;

import static com.usermanagement.app.userservice.domain.config.AuthorizationEnum.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.usermanagement.app.userservice.application.support.HeaderTools;
import com.usermanagement.app.userservice.application.user.api.doc.UserAdminControllerDoc;
import com.usermanagement.app.userservice.application.config.UriPathConsts;
import com.usermanagement.app.userservice.application.user.factory.UserRequestResponseFactory;
import com.usermanagement.app.userservice.application.user.model.PagingResponse;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import com.usermanagement.app.userservice.domain.common.PagingQuery;
import com.usermanagement.app.userservice.domain.common.PagingResult;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.domain.user.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Validated
@RequiredArgsConstructor
public class UserAdminController implements UserAdminControllerDoc {

  private final UserService userService;
  private final UserRequestResponseFactory userFactory;

  @GetMapping(path = UriPathConsts.ADMIN_USER, produces = MediaType.APPLICATION_JSON_VALUE)
  @Override
  public PagingResponse<UserResponse> getAllUsers(
      @RequestParam(name = "size", defaultValue = "25") int pageSize,
      @RequestParam(name = "page", defaultValue = "0") int pageNumber,
      @RequestParam(name = "query", required = false) String userQuery) {
    PagingResult<User> page = userService.getUsers(PagingQuery.of(pageSize, pageNumber), userQuery);

    List<UserResponse> users = page.getContent()
        .stream()
        .map(userFactory::toUserResponse)
        .collect(Collectors.toList());
    return PagingResponse.of(users, page.getTotalElements(), page.getTotalPages());
  }

  @PostMapping(path = UriPathConsts.ADMIN_SET_ROLE)
  @Override
  public ResponseEntity<Void> setUserRole(@RequestParam String userId, @RequestParam String userRole) {

    AuthorizationEnum role = HeaderTools.getUserRole(userRole);
    if (!userService.setUserRole(userId, role)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = UriPathConsts.ADMIN_DELETE_USER)
  public ResponseEntity<Void> deleteUser(@RequestHeader String userId) {
    if (userService.getUser(userId).isEmpty()) {
      throw new ResponseStatusException(BAD_REQUEST,
          String.format("There exists no user with the ID '%s'.", userId));
    }
    userService.deleteUser(userId, UserDeletionStatus.ADMIN.toString());
    return ResponseEntity.ok().build();
  }

}

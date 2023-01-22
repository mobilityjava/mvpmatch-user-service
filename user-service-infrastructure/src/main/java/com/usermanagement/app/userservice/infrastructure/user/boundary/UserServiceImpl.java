package com.usermanagement.app.userservice.infrastructure.user.boundary;

import com.usermanagement.app.userservice.domain.common.PagingQuery;
import com.usermanagement.app.userservice.domain.common.PagingResult;
import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.domain.user.UserService;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import com.usermanagement.app.userservice.infrastructure.user.factory.UserFactory;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;
  private final UserFactory userFactory;
  private final UserAdmin userAdmin;

  @Override
  public Optional<User> getUser(String userId) {

    Optional<UserDocument> byUserId = userRepository.findByUserIdAndDeletedContains(userId, UserDeletionStatus.ACTIVE.toString());
    return byUserId.map(userFactory::toUser);
  }

  @Override
  public Optional<User> createUser(@NonNull User user) {
    var savedUser = userRepository.save(userFactory.toUserDTO(user));
    return Optional.ofNullable(userFactory.toUser(savedUser));
  }

  @Override
  public Optional<User> updateUser(@NonNull User user) {
    final var byUserId = userRepository.findByUserIdAndDeletedContains(user.getUserId(), UserDeletionStatus.ACTIVE.toString());
    if (byUserId.isEmpty()) {
      return Optional.empty();
    }
    final var storedUser = byUserId.get();
    final var newUser = userFactory.toUserDTO(user);
    newUser.setUserDTOId(storedUser.getUserDTOId());
    var savedUser = userRepository.save(newUser);
    return Optional.ofNullable(userFactory.toUser(savedUser));
  }

  @Override
  public void deleteUser(@NonNull String userId, @NonNull String deletedByWhom) {
    userAdmin.deleteFirebaseUser(userId);

    final var currentUser = userRepository.findByUserIdAndDeletedContains(userId,
        UserDeletionStatus.ACTIVE.toString());

    if (currentUser.isPresent()) {

      final var deletedUser = userFactory.toDeletedUserDTO(currentUser.get(), deletedByWhom);

      userRepository.save(deletedUser);

    } else {
      throw new IllegalArgumentException(String.format("User '%s' could not be found.", userId));
    }
  }

  @Override
  public void deleteFirebaseUser(@NonNull String userId) {
    userAdmin.deleteFirebaseUser(userId);
  }

  @Override
  public PagingResult<User> getUsers(PagingQuery pagingQuery, String userQuery) {
    PageRequest pageRequest = PageRequest.of(pagingQuery.getNumber(), pagingQuery.getSize(),
        Sort.by(UserDocument.FIELD_EMAIL).ascending());

    final var page = (userQuery == null || userQuery.isBlank()) ?
        userRepository.findAll(pageRequest) :
        userRepository.findByNameContainingOrEmailContainingOrUserIdContaining(userQuery, userQuery, userQuery, pageRequest);
    var users = page.getContent()
        .stream()
        .map(userFactory::toUser)
        .collect(Collectors.toList());
    return PagingResult.of(users, page.getTotalElements(), page.getTotalPages());
  }

  @Override
  public boolean setUserRole(String userId, AuthorizationEnum role) {
    userAdmin.setUserRole(userId, role);
    return true;
  }

  @Override
  public boolean exists(String userId) {
    return userRepository.existsByUserIdAndDeletedContains(userId, UserDeletionStatus.ACTIVE.toString());
  }

}

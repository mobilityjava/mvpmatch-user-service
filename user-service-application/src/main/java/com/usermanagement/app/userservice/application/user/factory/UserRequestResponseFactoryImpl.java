package com.usermanagement.app.userservice.application.user.factory;

import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import com.usermanagement.app.userservice.domain.config.TimeConfig;
import com.usermanagement.app.userservice.domain.user.User;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class UserRequestResponseFactoryImpl implements UserRequestResponseFactory {


  @Override
  public User toUserUpdate(UserRequest userRequest,
      String userId,
      String email,
      User currentUser) {

    User.UserBuilder user = User.builder();

    user.userId(userId);
    user.name(
        (Objects.isNull(userRequest.getName()) ? currentUser.getName() : userRequest.getName()));
    // We always wanted the Email in lowercase and trim
    user.email(email.toLowerCase(Locale.ROOT).trim());
    user.lastName((Objects.isNull(userRequest.getLastName()) ? currentUser.getLastName()
        : userRequest.getLastName()));
    // User can not update balance with Update endpoint
    user.balance(currentUser.getBalance());
    user.birthday((userRequest.getBirthday() != null) ?
        LocalDate.parse(userRequest.getBirthday(), TimeConfig.ISO_LOCAL_DATE_FORMATTER)
        : currentUser.getBirthday());
    user.addressStreet(
        (Objects.isNull(userRequest.getAddress_street())) ? currentUser.getAddressStreet()
            : userRequest.getAddress_street());
    user.addressZip((Objects.isNull(userRequest.getAddress_zip())) ? currentUser.getAddressZip()
        : userRequest.getAddress_zip());
    user.addressCity((Objects.isNull(userRequest.getAddress_city())) ? currentUser.getAddressCity()
        : userRequest.getAddress_city());
    user.addressCountry(
        (Objects.isNull(userRequest.getAddress_country())) ? currentUser.getAddressCountry()
            : userRequest.getAddress_country());

    user.generalTerms(currentUser.getGeneralTerms());
    user.dataProtection(currentUser.getDataProtection());

    return user.build();
  }

  @Override
  public User toUserWithDeposit(Integer validValue, String userId, User currentUser) {
    User.UserBuilder user = User.builder();

    user.userId(userId);
    user.name(currentUser.getName());
    user.lastName(currentUser.getLastName());
    user.email(currentUser.getEmail());
    user.balance((validValue == 0) ? 0 : validValue + currentUser.getBalance());
    user.birthday(currentUser.getBirthday());
    user.addressStreet(currentUser.getAddressStreet());
    user.addressZip(currentUser.getAddressZip());
    user.addressCity(currentUser.getAddressCity());
    user.addressCountry(currentUser.getAddressCountry());
    user.generalTerms(currentUser.getGeneralTerms());
    user.dataProtection(currentUser.getDataProtection());

    return user.build();
  }

  @Override
  public User toUser(UserRequest userRequest,
      String userId,
      String email,
      OffsetDateTime generalTerms,
      OffsetDateTime dataProtection) {

    User.UserBuilder user = User.builder();

    user.userId(userId);
    user.name(userRequest.getName());
    // We always wanted the Email in lowercase and trim
    user.email(email.toLowerCase(Locale.ROOT).trim());
    user.lastName(userRequest.getLastName());
    if (userRequest.getBirthday() != null) {
      user.birthday(
          LocalDate.parse(userRequest.getBirthday(), TimeConfig.ISO_LOCAL_DATE_FORMATTER));
    }
    user.addressStreet(userRequest.getAddress_street());
    user.addressZip(userRequest.getAddress_zip());
    user.addressCity(userRequest.getAddress_city());
    user.addressCountry(userRequest.getAddress_country());
    user.generalTerms(generalTerms);
    user.dataProtection(dataProtection);


    return user.build();
  }

  @Override
  public UserResponse toUserResponse(User user) {

    UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

    userResponse.id(user.getUserId());
    userResponse.name(user.getName());
    if(user.getEmail() != null){
      userResponse.email(user.getEmail());
    }
    userResponse.lastName(user.getLastName());
    userResponse.balance(String.valueOf(user.getBalance()));
    if (user.getBirthday() != null) {
      userResponse.birthday(user.getBirthday().format(TimeConfig.ISO_LOCAL_DATE_FORMATTER));
    }
    userResponse.address_street(user.getAddressStreet());
    userResponse.address_zip(user.getAddressZip());
    userResponse.address_city(user.getAddressCity());
    userResponse.address_country(user.getAddressCountry());
    if (user.getGeneralTerms() != null) {
      userResponse.permissions_general_terms(
          user.getGeneralTerms().format(TimeConfig.ISO_DATE_TIME_FORMATTER));
    }
    if (user.getDataProtection() != null) {
      userResponse.permissions_data_protection(
          user.getDataProtection().format(TimeConfig.ISO_DATE_TIME_FORMATTER));
    }

    return userResponse.build();
  }

}

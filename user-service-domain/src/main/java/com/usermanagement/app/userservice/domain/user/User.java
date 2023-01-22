package com.usermanagement.app.userservice.domain.user;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Represents user
 */
@Value
@Builder
public class User {

  @NonNull
  String userId;

  String name;

  String lastName;

  String email;

  Integer balance;

  LocalDate birthday;

  String addressStreet;

  String addressZip;

  String addressCity;

  String addressCountry;

  OffsetDateTime generalTerms;

  OffsetDateTime dataProtection;

}

package com.usermanagement.app.userservice.application.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


/**
 * UI model for a user's profile response.
 */
@Schema(name = "UserResponse", description = "Represents a user's profile on a response.", accessMode = AccessMode.READ_ONLY)
@Value
@Builder
public class UserResponse {

  public static final String RELEASE_OF_JAVA_BACKEND = "2022-07-20T11:00:00Z";
  @Schema(description = "User's id.", required = true, example = "00G6F8GOhXTeD6xQLslhBeryzos2")
  @NonNull
  String id;

  @Schema(description = "User's e-mail address", example = "john.doe@no.op")
  @Builder.Default
  String email = ""; // empty String, since email should be mandatory, but it's sometimes null

  @Schema(description = "User's first name", example = "John")
  String name;

  @Schema(description = "User's last name", example = "Doe")
  String lastName;

  @Schema(description = "User's current balance", example = "100")
  String balance;

  @Schema(description = "User's date of birth", example = "2000-04-01")
  String birthday;

  @Schema(description = "Part of user's address: street", example = "Weinbergstr. 18")
  String address_street;

  @Schema(description = "Part of user's address: zip code", example = "01234")
  String address_zip;

  @Schema(description = "Part of user's address: city", example = "Weinstadt")
  String address_city;

  @Schema(description = "Part of user's address: country", example = "Deutschland")
  String address_country;

  @Schema(description = "Date and time of user's consent to general terms", example = "2000-04-01T12:00:00Z")
  @Builder.Default
  String permissions_general_terms = RELEASE_OF_JAVA_BACKEND; // some legacy users do not have those dates set, but they are mandatory in the Apps

  @Schema(description = "Date and time of user's consent to data protection", example = "2000-04-01T12:00:00Z")
  @Builder.Default
  String permissions_data_protection = RELEASE_OF_JAVA_BACKEND; // some legacy users do not have those dates set, but they are mandatory in the Apps

}

package com.usermanagement.app.userservice.application.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * UI model for a user's profile request.
 */
@Schema(name = "UserRequest", description = "Represents a user's profile on a request.")
@Value
@Builder
@With
public class UserRequest {

    @Schema(description = "User's first name", example = "John")
    String name;

    @Schema(description = "User's last Name", example = "Doe")
    String lastName;

    @Schema(description = "User's email", example = "john.doe@example.org")
    String email;

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

}

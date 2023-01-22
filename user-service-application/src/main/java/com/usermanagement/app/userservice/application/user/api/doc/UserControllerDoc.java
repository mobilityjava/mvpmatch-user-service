package com.usermanagement.app.userservice.application.user.api.doc;

import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "User self management")
public interface UserControllerDoc {

  @Operation(summary = "Get current user's profile.", security = {
      @SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "404", description = "User profile could not be found.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  UserResponse currentUser(Map<String, String> headers);

  @Operation(summary = "Create current user's profile.", security = {@SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "400", description = "Bad request, mismatched data.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  ResponseEntity<UserResponse> createUser(UserRequest request, Map<String, String> headers);

  @Operation(summary = "Update current user's profile.", security = {@SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "400", description = "Bad request, mismatched data.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "409", description = "E-Mail is already in use.")
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  ResponseEntity<UserResponse> updateUser(UserRequest request, Map<String, String> headers);

  @Operation(summary = "Deposit balance for user.", security = {@SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "400", description = "Bad request, mismatched data.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  ResponseEntity<UserResponse> deposit(@Parameter String depositValue, Map<String, String> headers);

  @Operation(summary = "Deposit balance for user.", security = {@SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "400", description = "Bad request, mismatched data.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  ResponseEntity<UserResponse> depositReset(Map<String, String> headers);

  @Operation(summary = "Delete current user's profile.", security = {@SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  ResponseEntity<Void> deleteUser(Map<String, String> headers);

}

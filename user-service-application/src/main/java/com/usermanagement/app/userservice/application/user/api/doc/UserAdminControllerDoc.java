package com.usermanagement.app.userservice.application.user.api.doc;

import com.usermanagement.app.userservice.application.user.model.PagingResponse;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User Admin", description = "User administration")
public interface UserAdminControllerDoc {

  @Operation(summary = "Get all users", security = {
      @SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "list of all user with pagination")
  @ApiResponse(responseCode = "401", description = "user not authenticated", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "internal error", content = @Content(schema = @Schema(hidden = true)))
  PagingResponse<UserResponse> getAllUsers(
      @Parameter(description = "Maximum items to display on page.") int size,
      @Parameter(description = "Number of page to return (starting with 0).") int page,
      @Parameter(description = "Query string for searching user based on Email or Name or UserId") String query);

  @Operation(summary = "Set Role to user.", security = {
      @SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Set Role to user")
  @ApiResponse(responseCode = "400", description = "UserId not valid")
  @ApiResponse(responseCode = "404", description = "User not found")
  @ApiResponse(responseCode = "500", description = "Internal error, try again")
  ResponseEntity<Void> setUserRole(
      @Parameter(required = true, name = "userId", description = "Firebase ID of user", example = "lS3ARWUIk3aSaA0akWlgdIwmVtF2") String userId,
      @Parameter(required = true, name = "userRole", description = "user role", example = "seller") String userRole);

  @Operation(summary = "Delete user profile.", security = {
      @SecurityRequirement(name = "bearer-key")})
  @ApiResponse(responseCode = "200", description = "Successful operation.")
  @ApiResponse(responseCode = "401", description = "User is not authorized.", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(hidden = true)))
  ResponseEntity<Void> deleteUser(
      @Parameter(required = true, name = "userId", description = "Id of user that should be deleted") String userId);

}

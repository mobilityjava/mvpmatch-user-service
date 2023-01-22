package com.usermanagement.app.userservice.application.user.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.usermanagement.app.userservice.application.ApplicationIT;
import com.usermanagement.app.userservice.application.config.HeaderConsts;
import com.usermanagement.app.userservice.application.config.UriPathConsts;
import com.usermanagement.app.userservice.application.support.UserMock;
import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.utils.RestRequestUtils;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import com.usermanagement.app.userservice.application.config.ExceptionAdvice;
import com.usermanagement.app.userservice.application.support.Matchers;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class UserControllerIT extends ApplicationIT {

  private static final String USER_ID = UserMock.getInstance().getUserId();
  private static final String USER_ID2 = "userId2";
  private static final boolean EMAIL_VERIFIED = true;
  private static final String EMAIL = "email@no.op";
  private static final String VALID_PASSWORD = "AAAbbbccc[123";
  private static final String INVALID_PASSWORD = "12345678";

  @Autowired
  UserController userController;

  @DisplayName("Get current User should succeed with 200 code")
  @Test
  void getCurrentUserShouldSucceedWith200() throws Exception {
    UserDocument userDTO = UserMock.getInstance().getUserDocument1();
    when(userRepository.findByUserIdAndDeletedContains(USER_ID, UserDeletionStatus.ACTIVE.toString()))
        .thenReturn(Optional.ofNullable(userDTO));

    mockMvc
        .perform(MockMvcRequestBuilders.get(UriPathConsts.CURRENT_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(userDTO.getUserId())))
        .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
        .andExpect(jsonPath("$.birthday",
            is(userDTO.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE))))
        .andExpect(jsonPath("$.permissions_general_terms",
            is((OffsetDateTime.of(userDTO.getPermissionsGeneralTerms(), ZoneOffset.UTC))
                .toString())));
  }

  @DisplayName("Get current User should return 404 if User has no profile and not found.")
  @Test
  void getCurrentUserShouldFailWith404IfUserNotFound() throws Exception {
    when(userRepository.findByUserIdAndDeletedContains(USER_ID, UserDeletionStatus.ACTIVE.toString())).thenReturn(Optional.empty());

    mockMvc
        .perform(get(UriPathConsts.CURRENT_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
    ;
  }

  @Test
  void getCurrentUserShouldFailWith500OnException() throws Exception {
    doThrow(new RuntimeException("some error")).when(userRepository)
        .findByUserIdAndDeletedContains(anyString(), anyString());

    mockMvc
        .perform(get(UriPathConsts.CURRENT_USER))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(content().string(ExceptionAdvice.INTERNAL_SERVER_ERROR_MSG));
  }

  @DisplayName("Create User should succeed with 200 code")
  @Test
  void createUserShouldSucceedWith200() throws Exception {
    UserDocument userDTO = UserMock.getInstance().getUserDocument1();
    UserRequest userRequest = UserMock.getInstance().getUserRequest();

    when(userRepository.save(any(UserDocument.class))).thenReturn(userDTO);

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userDTO.getUserId())))
        .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
        .andExpect(jsonPath("$.birthday",
            is(userDTO.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE))))
        .andExpect(jsonPath("$.permissions_general_terms",
            is((OffsetDateTime.of(userDTO.getPermissionsGeneralTerms(), ZoneOffset.UTC))
                .toString())));
  }

  @DisplayName("Create User with Email from Body should succeed with 200 code")
  @Test
  void createUserWithEmailFromBodyShouldSucceedWith200() throws Exception {
    UserDocument userDTO = UserMock.getInstance().getUserDocumentWithEmail();
    UserRequest userRequest = UserMock.getInstance().getUserRequestWithEmail();

    when(userRepository.save(any(UserDocument.class))).thenReturn(userDTO);

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userDTO.getUserId())))
        .andExpect(jsonPath("$.email", is(userDTO.getEmail())));
  }

  @DisplayName("Create User with Email on body and header should succeed with 200 code")
  @Test
  void createUserWithEmailFromBodyAndHeaderShouldSucceedWith200() throws Exception {
    UserDocument userDTO = UserMock.getInstance().getUserDocumentWithEmail();
    UserRequest userRequest = UserMock.getInstance().getUserRequestWithEmail();

    when(userRepository.save(any(UserDocument.class))).thenReturn(userDTO);

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, userRequest.getEmail())
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userDTO.getUserId())))
        .andExpect(jsonPath("$.email", is(userDTO.getEmail())));
  }

  @DisplayName("Create User should fail with 409 if already exists")
  @Test
  void createUserShouldFailWith409IfIdAlreadyExists() throws Exception {
    UserRequest userRequest = UserMock.getInstance().getUserRequest();

    when(userRepository.existsByUserIdAndDeletedContains(eq(USER_ID),anyString())).thenReturn(true);

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isConflict());
  }

  @DisplayName("Create User should fail with 404 code if userId is null")
  @Test
  void createUserShouldFailWith404IfUserIdNull() throws Exception {
    UserRequest userRequest = UserMock.getInstance().getUserRequest();

    // UserId header is omitted here in order to simulate that userId is null
    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is4xxClientError());
  }

  @DisplayName("Create User should fail with 400 code if UserRequest is NUll")
  @Test
  void createUserShouldFailWith400IfUserRequestIsNull() throws Exception {

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(null))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is4xxClientError());
  }

  @DisplayName("Create User with Email should fail with 400 code if email from body and header do not match")
  @Test
  void createUserWithEmailShouldFailWith400IfEmailFromHeaderAndBodyDontMatch() throws Exception {
    UserRequest userRequest = UserMock.getInstance().getUserRequestWithEmail();

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, "OtherMail")
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest()).andExpect(Matchers.contentIsEmpty());
  }

  @DisplayName("Create User with Email should fail with 500 code if email is null")
  @Test
  void createUserWithEmailShouldFailWith500IfEmailIsNull() throws Exception {
    UserRequest userRequest = UserMock.getInstance().getUserRequest();

    mockMvc
        .perform(post(UriPathConsts.CREATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isInternalServerError()).andExpect(Matchers.contentIsEmpty());
  }

  @DisplayName("Update User should succeed with 200 code")
  @Test
  void updateUserShouldSucceedWith200() throws Exception {

    UserRequest userRequest = UserMock.getInstance().getUserRequest();
    UserDocument userDTO = UserMock.getInstance().getUserDocument2();

    when(userRepository.findByUserIdAndDeletedContains(USER_ID, UserDeletionStatus.ACTIVE.toString())).thenReturn(
        Optional.ofNullable(userDTO));
    when(userRepository.save(any(UserDocument.class))).thenReturn(userDTO);

    mockMvc
        .perform(put(UriPathConsts.UPDATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, USER_ID)
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userDTO.getUserId())))
        .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
        .andExpect(jsonPath("$.birthday",
            is(userDTO.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE))))
        .andExpect(jsonPath("$.permissions_general_terms",
            is((OffsetDateTime.of(userDTO.getPermissionsGeneralTerms(), ZoneOffset.UTC))
                .toString())));
  }

  @DisplayName("Update User should fail with 404 code, No user with id 'userId' exists")
  @Test
  void updateUserShouldFailWith404() throws Exception {
    UserRequest userRequest = UserMock.getInstance().getUserRequest();

    when(userRepository.findByUserIdAndDeletedContains(anyString(), anyString()))
        .thenReturn(Optional.empty());

    mockMvc
        .perform(put(UriPathConsts.UPDATE_USER)
            .header(HeaderConsts.HEADER_USER_ID, "someId")
            .header(HeaderConsts.HEADER_EMAIL, EMAIL)
            .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED)
            .content(RestRequestUtils.asJsonString(userRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is4xxClientError())
        .andExpect(Matchers.contentIsEmpty())
    ;
  }

  @DisplayName("Delete current User should succeed with 200 code")
  @Test
  void deleteUserShouldSucceedWith200() throws Exception {
    UserDocument userDTO = UserMock.getInstance().getUserDocument2();
    when(userRepository.findByUserIdAndDeletedContains(USER_ID, UserDeletionStatus.ACTIVE.toString()))
        .thenReturn(Optional.ofNullable(userDTO));

    mockMvc
        .perform(delete(UriPathConsts.DELETE_USER)
                .header(HeaderConsts.HEADER_USER_ID, USER_ID)
                .header(HeaderConsts.HEADER_EMAIL, EMAIL)
                .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED))
        .andExpect(status().isOk())
        .andExpect(Matchers.contentIsEmpty());

  }

  @DisplayName("Delete nonexistent User should Delete the firebase user deletion with 200 code")
  @Test
  void deleteUserShouldSucceedWith200IfUserProfileIsNonexistent() throws Exception {
    when(userRepository.findByUserIdAndDeletedContains(USER_ID, UserDeletionStatus.ACTIVE.toString())).thenReturn(Optional.empty());

    mockMvc
        .perform(delete(UriPathConsts.DELETE_USER)
                .header(HeaderConsts.HEADER_USER_ID, USER_ID)
                .header(HeaderConsts.HEADER_EMAIL, EMAIL)
                .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED))
        .andExpect(status().isOk())
        .andExpect(Matchers.contentIsEmpty());
  }

  @DisplayName("Delete current User should fail with 500 code on error")
  @Test
  void deleteUserShouldFailWith500OnException() throws Exception {
    UserDocument userDTO = UserMock.getInstance().getUserDocument2();
    when(userRepository.findByUserIdAndDeletedContains(USER_ID, UserDeletionStatus.ACTIVE.toString())).thenReturn(Optional.ofNullable(userDTO));
    doThrow(new RuntimeException("some error")).when(userRepository).save(any());

    mockMvc
        .perform(delete(UriPathConsts.CURRENT_USER)
                .header(HeaderConsts.HEADER_USER_ID, USER_ID)
                .header(HeaderConsts.HEADER_EMAIL, EMAIL)
                .header(HeaderConsts.HEADER_EMAIL_VERIFIED, EMAIL_VERIFIED))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(content().string(ExceptionAdvice.INTERNAL_SERVER_ERROR_MSG));
  }


}

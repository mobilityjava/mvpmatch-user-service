package com.usermanagement.app.userservice.application.user.api;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.usermanagement.app.userservice.application.ApplicationIT;
import com.usermanagement.app.userservice.application.config.ExceptionAdvice;
import com.usermanagement.app.userservice.application.config.UriPathConsts;
import com.usermanagement.app.userservice.application.support.Matchers;
import com.usermanagement.app.userservice.application.support.UserMock;
import com.usermanagement.app.userservice.domain.config.AuthorizationEnum;
import com.usermanagement.app.userservice.domain.config.TimeConfig;
import com.usermanagement.app.userservice.infrastructure.user.boundary.UserAdmin;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class UserAdminControllerIT extends ApplicationIT {

  @SpyBean
  UserAdmin userAdmin;

  @DisplayName("Get all users should succeed with 200 code and paged result")
  @Test
  void getAllUsersShouldSucceedWith200() throws Exception {
    final var userMock = UserMock.getInstance();
    List<UserDocument> users = userMock.getUsers();
    when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(users));

    mockMvc
        .perform(MockMvcRequestBuilders.get(UriPathConsts.ADMIN_USER))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.totalPages", is(1)))
        .andExpect(jsonPath("$.totalElements", is(2)))
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].id", is(userMock.getUser1().getUserId())))
        .andExpect(jsonPath("$.content[0].email", is(userMock.getUser1().getEmail())))
        .andExpect(jsonPath("$.content[0].birthday",
            is(userMock.getUser1().getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE))))
        .andExpect(jsonPath("$.content[0].permissions_general_terms",
            is(userMock.getUser1().getGeneralTerms().format(TimeConfig.ISO_DATE_TIME_FORMATTER))));
  }

  @DisplayName("Get all users should succeed with 200 code and paged result and filter the result based on Name")
  @Test
  void getAllUsersShouldSucceedWith200WithFilteringTheResult() throws Exception {
    final var userMock = UserMock.getInstance();
    List<UserDocument> user = userMock.getUser();
    String query = userMock.getDefaultEmail();
    when(userRepository.findByNameContainingOrEmailContainingOrUserIdContaining(eq(query), eq(query), eq(query), any(
        PageRequest.class))).thenReturn(new PageImpl<>(user));

    mockMvc
        .perform(get(UriPathConsts.ADMIN_USER)
            .param("size", "25")
            .param("page", "0")
            .param("query", query))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.totalPages", is(1)))
        .andExpect(jsonPath("$.totalElements", is(1)))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id", is(userMock.getUser1().getUserId())))
        .andExpect(jsonPath("$.content[0].email", is(userMock.getUser1().getEmail())))
        .andExpect(jsonPath("$.content[0].birthday",
            is(userMock.getUser1().getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE))))
        .andExpect(jsonPath("$.content[0].permissions_general_terms",
            is(userMock.getUser1().getGeneralTerms().format(TimeConfig.ISO_DATE_TIME_FORMATTER))));
  }

  @DisplayName("Get all users should succeed with 200 code and empty paged result")
  @Test
  void getAllUsersWithEmptyResultShouldSucceedWith200() throws Exception {
    when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

    mockMvc
        .perform(get(UriPathConsts.ADMIN_USER))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.totalPages", is(1)))
        .andExpect(jsonPath("$.totalElements", is(0)))
        .andExpect(jsonPath("$.content", empty()));
  }

  @DisplayName("Get all users should fail with 500 code")
  @Test
  void getAllUsersShouldFailWith500OnException() throws Exception {
    doThrow(new RuntimeException("some error")).when(userRepository).findAll(any(Pageable.class));

    mockMvc
        .perform(get(UriPathConsts.ADMIN_USER))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(ExceptionAdvice.INTERNAL_SERVER_ERROR_MSG));
  }

  @Test
  void setUserRoleShouldShouldSucceedWith200() throws Exception {

    mockMvc
        .perform(post(UriPathConsts.ADMIN_SET_ROLE)
            .param("userId", "userId")
            .param("userRole", "admin")
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andExpect(Matchers.contentIsEmpty());

    verify(userAdmin, times(1)).setUserRole("userId", AuthorizationEnum.ADMIN_ROLE);
  }


  @DisplayName("set User Role should fail with 500 code on exception")
  @Test
  void setUserRoleShouldFailWith500IfExceptionIsThrown() throws Exception {
    doThrow(new RuntimeException("some error"))
        .when(userAdmin).setUserRole(any(), any());

    mockMvc
        .perform(post(UriPathConsts.ADMIN_SET_ROLE)
            .param("userId", "userId")
            .param("userRole", "admin")
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(ExceptionAdvice.INTERNAL_SERVER_ERROR_MSG));
  }

}

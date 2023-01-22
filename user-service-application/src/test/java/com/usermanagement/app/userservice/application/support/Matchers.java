package com.usermanagement.app.userservice.application.support;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.web.servlet.ResultMatcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Matchers {

  public static ResultMatcher contentIsEmpty() {
    return jsonPath("$").doesNotExist();
  }

}

package com.usermanagement.app.userservice.application.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Utility class for rest api test.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestRequestUtils {

  /**
   * Convert an object to a json string.
   *
   * @param obj object to convert
   * @return json string
   * @throws JsonProcessingException if the object cannot be convert into a json string
   */
  public static String asJsonString(final Object obj) throws JsonProcessingException {

    return new ObjectMapper().writeValueAsString(obj);
  }

  public static ResultMatcher contentIsEmpty() {
    return jsonPath("$").doesNotExist();
  }

}

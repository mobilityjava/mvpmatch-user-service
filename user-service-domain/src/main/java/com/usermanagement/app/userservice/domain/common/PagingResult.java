package com.usermanagement.app.userservice.domain.common;

import java.util.List;
import lombok.Value;

/**
 * Represents a generic paging result.
 */
@Value(staticConstructor = "of")
public class PagingResult<T> {

  List<T> content;

  long totalElements;

  int totalPages;

}

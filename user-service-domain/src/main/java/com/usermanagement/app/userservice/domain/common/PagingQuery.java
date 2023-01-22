package com.usermanagement.app.userservice.domain.common;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Represents a generic paging query.
 */
@Value
@RequiredArgsConstructor(staticName = "of")
public class PagingQuery {

  int size;

  int number;

}

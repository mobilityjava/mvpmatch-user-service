package com.usermanagement.app.userservice.application.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Value;

/**
 * Generic paging response containing content for the requested page and information about the total
 * result.
 *
 * @param <T> type of content
 */
@Value(staticConstructor = "of")
public class PagingResponse<T> {

  @Schema(description = "Content of the current page.")
  List<T> content;

  @Schema(description = "Total element count.", example = "120")
  long totalElements;

  @Schema(description = "Total page count.", example = "12")
  int totalPages;

}


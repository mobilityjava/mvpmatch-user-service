package com.usermanagement.app.userservice.domain.config;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Configurations for date and time handling.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeConfig {

  public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");

  public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssX";
  public static final String ISO_LOCAL_DATE = "yyyy-MM-dd";

  public static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
      TimeConfig.ISO_DATE_TIME);
  public static final DateTimeFormatter ISO_LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(
      TimeConfig.ISO_LOCAL_DATE);

  /**
   * Set default timezone.
   */
  public static void configureDefaultTimeZone() {
    TimeZone.setDefault(DEFAULT_TIMEZONE);
  }
}

package com.usermanagement.app.userservice.domain.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class TimeConfigTest {

    @DisplayName("should pass if local date format matches")
    @Test
    void shouldPassIfCorrectLocalDateFormat() {

        String date = "2000-05-31";
        LocalDate actual = LocalDate.parse(date, TimeConfig.ISO_LOCAL_DATE_FORMATTER);
        LocalDate expected = LocalDate.of(2000, 5, 31);
        
        assertNotNull(actual);
        assertEquals(actual, expected);

    }

    @DisplayName("should pass if date time format matches")
    @Test
    void shouldPassIfCorrectDateTimeFormat() {

        String actual = "2017-05-12T05:45:00+0630";
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDateTime.of(2017, 05, 12, 05, 45),
                ZoneOffset.ofHoursMinutes(6, 30));
        String expected = offsetDateTime.format(TimeConfig.ISO_DATE_TIME_FORMATTER);

        assertEquals(actual, expected);

    }

    @DisplayName("should match configured Default Time Zone")
    @Test
    void shouldMatchConfiguredTimeZone() {

        TimeConfig.configureDefaultTimeZone();

        TimeZone actual = TimeZone.getDefault();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        assertNotNull(actual);
        assertEquals(timeZone, actual);
    }


}

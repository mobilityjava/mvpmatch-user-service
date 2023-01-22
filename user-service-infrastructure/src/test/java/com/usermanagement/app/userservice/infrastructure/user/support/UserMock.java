package com.usermanagement.app.userservice.infrastructure.user.support;

import com.usermanagement.app.userservice.domain.config.TimeConfig;
import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

public final class UserMock {

    @Getter
    private final User user;
    private final String dateInput = "2016-01-01T12:00:01Z";

    public UserMock() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        this.user = User.builder()
            .userId("complexIdHere456")
            .name("name-1")
            .email("default@email.com")
            .balance(100)
            .lastName("Doe-1")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-1")
            .addressZip("addressZip-1")
            .addressCity("addressCity-1")
            .addressCountry("addressCountry-1")
            .generalTerms(OffsetDateTime.parse(dateInput, dateFormat))
            .dataProtection(OffsetDateTime.parse(dateInput, dateFormat))
            .build();

    }

    /**
     * @return Since UserDocument is mutable, we need to return a fresh instance each time it's used
     */
    public UserDocument getUserDTO() {
        return UserDocument.builder()
            .userId("complexIdHere456")
            .name("name-1")
            .email("default@email.com")
            .balance("100")
            .lastName("Doe-1")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-1")
            .addressZip("addressZip-1")
            .addressCity("addressCity-1")
            .addressCountry("addressCountry-1")
            .deleted(UserDeletionStatus.ACTIVE.toString())
            .permissionsGeneralTerms(
                LocalDateTime.parse(dateInput, TimeConfig.ISO_DATE_TIME_FORMATTER))
            .permissionsDataProtection(
                LocalDateTime.parse(dateInput, TimeConfig.ISO_DATE_TIME_FORMATTER))
            .build();
    }

    /**
     * Returns the instance.
     *
     * @return Instance on {@link UserMock}
     */
    public static UserMock getInstance() {

        return InstanceHolder.instance;
    }

    private static final class InstanceHolder {

        private static final UserMock instance =
            new UserMock();
    }
}

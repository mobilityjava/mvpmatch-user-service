package com.usermanagement.app.userservice.application.support;

import com.usermanagement.app.userservice.application.user.model.UserRequest;
import com.usermanagement.app.userservice.application.user.model.UserResponse;
import com.usermanagement.app.userservice.domain.config.TimeConfig;
import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;

public final class UserMock {

    @Getter
    private final UserDocument userDocument1;
    @Getter
    private final UserDocument userDocument2;
    @Getter
    private final UserDocument userDocumentWithEmail;

    @Getter
    private final List<UserDocument> users;

    @Getter
    private final List<UserDocument> user;

    @Getter
    private final User user1;

    @Getter
    private final User user2;

    @Getter
    private final UserResponse userResponse;

    @Getter
    private final UserRequest userRequest;

    @Getter
    private final UserRequest userRequestWithEmail;

    private final String dateInput = "2016-01-01T12:00:01Z";
    private final DateTimeFormatter dateFormat = TimeConfig.ISO_DATE_TIME_FORMATTER;
    @Getter
    private final OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateInput, dateFormat);

    @Getter
    private final String userId = "123qwe";

    @Getter
    private final String defaultEmail = "default@email.com";


    public UserMock() {

        this.user1 = User.builder()
            .userId(userId)
            .name("name-1")
            .email(defaultEmail)
            .balance(100)
            .lastName("Doe-1")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-1")
            .addressZip("addressZip-1")
            .addressCity("addressCity-1")
            .addressCountry("addressCountry-1")
            .generalTerms(offsetDateTime)
            .dataProtection(offsetDateTime)
            .build();

        this.user2 = User.builder()
            .userId("userId2")
            .name("name-2")
            .email("some.mail@no.op")
            .balance(null)
            .lastName("Doe-2")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-2")
            .addressZip("addressZip-2")
            .addressCity("addressCity-2")
            .addressCountry("addressCountry-2")
            .generalTerms(offsetDateTime)
            .dataProtection(offsetDateTime)
            .build();


        this.userResponse = UserResponse.builder()
                .id(userId)
                .name("name-1")
                .email(defaultEmail)
                .balance("100")
                .lastName("Doe-1")
                .birthday("1990-01-01")
                .address_street("addressStreet-1")
                .address_zip("addressZip-1")
                .address_city("addressCity-1")
                .address_country("addressCountry-1")
                .permissions_general_terms(dateInput)
                .permissions_data_protection(dateInput)
                .build();

        this.userRequest = UserRequest.builder()
            .name("name-2")
            .lastName("Doe-2")
            .balance("100")
            .birthday("1990-01-01")
            .address_street("addressStreet-2")
            .address_zip("addressZip-2")
            .address_city("addressCity-2")
            .address_country("addressCountry-2")
            .build();

        this.userRequestWithEmail = UserRequest.builder()
            .name("name-2")
            .email("email")
            .build();

        userDocument1 = UserDocument.builder()
            .userId(userId)
            .name("name-1")
            .email(defaultEmail)
            .balance("100")
            .lastName("Doe-1")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-1")
            .addressZip("addressZip-1")
            .addressCity("addressCity-1")
            .addressCountry("addressCountry-1")
            .permissionsGeneralTerms(offsetDateTime.toLocalDateTime())
            .build();

        userDocumentWithEmail = UserDocument.builder()
            .userId(userId)
            .name("name-1")
            .email(defaultEmail)
            .balance("100")
            .lastName("Doe-1")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-1")
            .addressZip("addressZip-1")
            .addressCity("addressCity-1")
            .addressCountry("addressCountry-1")
            .permissionsGeneralTerms(offsetDateTime.toLocalDateTime())
            .permissionsDataProtection(offsetDateTime.toLocalDateTime())
            .build();

        userDocument2 = UserDocument.builder()
            .userId("userId-2")
            .name("name-2")
            .email("some.mail@no.op")
            .balance("100")
            .lastName("Doe-2")
            .birthday(LocalDate.parse("1990-01-01", TimeConfig.ISO_LOCAL_DATE_FORMATTER))
            .addressStreet("addressStreet-2")
            .addressZip("addressZip-2")
            .addressCity("addressCity-2")
            .addressCountry("addressCountry-2")
            .permissionsGeneralTerms(offsetDateTime.toLocalDateTime())
            .permissionsDataProtection(offsetDateTime.toLocalDateTime())
            .build();

        users = List.of(userDocument1, userDocument2);
        user = List.of(userDocument1);
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

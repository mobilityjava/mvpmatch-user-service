package com.usermanagement.app.userservice.infrastructure.user.persistence;

import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Users entry saved in Mongo.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "users")
public class UserDocument {

    public static final String FIELD_EMAIL = "email";
    /**
     * we will have 2 different ID for user
     * userDTOId is the DB ID for user
     */
    @Id
    private String userDTOId;

    /**
     * we will have 2 different ID for user
     * userId is the domain user ID (firebase ID) for user
     */
    @Indexed(unique = true)
    private String userId;

    private String name;

    @Indexed
    @Field(FIELD_EMAIL)
    private String email;

    private String lastName;

    private LocalDate birthday;

    private String balance;

    private String addressStreet;

    private String addressZip;

    private String addressCity;

    private String addressCountry;

    /**
     * The time that user accepted the Terms and Conditions
     */
    private LocalDateTime permissionsGeneralTerms;

    /**
     * The time that user accepted the Terms and DataProtection
     */
    private LocalDateTime permissionsDataProtection;

    /**
     * Indicates whether a user has been deleted or not.
     * Says "ACTIVE" if user has not been deleted.
     * Says "USER" if user deleted their own account.
     * Says "ADMIN" if user was deleted in the admin panel.
     */
    private String deleted;

    @CreatedDate
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime created;

    @LastModifiedDate
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime updated;

}

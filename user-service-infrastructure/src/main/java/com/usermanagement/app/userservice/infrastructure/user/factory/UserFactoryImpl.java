package com.usermanagement.app.userservice.infrastructure.user.factory;

import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.domain.config.UserDeletionStatus;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryImpl implements UserFactory{

    @Override
    public User toUser(@NonNull UserDocument userDTO) {

        User.UserBuilder user = User.builder();

        user.userId(userDTO.getUserId());
        user.name(userDTO.getName());
        user.email(userDTO.getEmail());
        user.balance(userDTO.getBalance() == null ? 0 : Integer.parseInt(
            userDTO.getBalance()));
        user.lastName(userDTO.getLastName());
        user.birthday(userDTO.getBirthday());
        user.addressStreet(userDTO.getAddressStreet());
        user.addressZip(userDTO.getAddressZip());
        user.addressCity(userDTO.getAddressCity());
        user.addressCountry(userDTO.getAddressCountry());

        LocalDateTime permissionsGeneralTerms = userDTO.getPermissionsGeneralTerms();
        if (permissionsGeneralTerms != null) {
            user.generalTerms(
                OffsetDateTime.of(permissionsGeneralTerms, ZoneOffset.UTC));
        }
        LocalDateTime permissionsDataProtection = userDTO.getPermissionsDataProtection();
        if (permissionsDataProtection != null) {
            user.dataProtection(
                OffsetDateTime.of(permissionsDataProtection, ZoneOffset.UTC));
        }

        return user.build();
    }

    @Override
    public List<User> toUserList(List<UserDocument> userDTOs) {
        return userDTOs.stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public UserDocument toUserDTO(User user) {

        UserDocument.UserDocumentBuilder userDTO = UserDocument.builder();

        userDTO.userId(user.getUserId());
        userDTO.name(user.getName());
        userDTO.email(user.getEmail());
        userDTO.balance(user.getBalance() == null ? "0" : String.valueOf(user.getBalance()));
        userDTO.lastName(user.getLastName());
        userDTO.birthday(user.getBirthday());
        userDTO.addressStreet(user.getAddressStreet());
        userDTO.addressZip(user.getAddressZip());
        userDTO.addressCity(user.getAddressCity());
        userDTO.addressCountry(user.getAddressCountry());
        userDTO.deleted(UserDeletionStatus.ACTIVE.toString());

        final var generalTerms = user.getGeneralTerms();
        if (generalTerms != null) {
            var utcGeneralTerms = LocalDateTime.ofInstant(generalTerms.toInstant(), ZoneOffset.UTC);
            userDTO.permissionsGeneralTerms(utcGeneralTerms);
        }

        final var dataProtectionTime = user.getDataProtection();
        if (dataProtectionTime != null) {
            var utcDataProtection = LocalDateTime
                .ofInstant(dataProtectionTime.toInstant(), ZoneOffset.UTC);
            userDTO.permissionsDataProtection(utcDataProtection);
        }

        return userDTO.build();
    }

    @Override
    public List<UserDocument> toUserDTOList(List<User> users) {
        return users.stream()
            .map(this::toUserDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserDocument toDeletedUserDTO(UserDocument userDTO, String deletedByWhom) {

        userDTO.setBirthday(null);
        userDTO.setLastName(null);
        userDTO.setAddressStreet(null);
        userDTO.setAddressCity(null);
        userDTO.setAddressZip(null);
        userDTO.setAddressCountry(null);

        userDTO.setDeleted(deletedByWhom);

        return userDTO;
    }

}

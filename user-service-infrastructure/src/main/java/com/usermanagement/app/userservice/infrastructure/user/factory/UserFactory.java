package com.usermanagement.app.userservice.infrastructure.user.factory;

import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFactory {

    /**
     * Creates a User from an UserDTO.
     *
     * @param userDTO of {@link UserDocument} type.
     * @return User.
     */
    User toUser(@NonNull UserDocument userDTO);

    /**
     * Creates a Users List from UserDTOs List.
     *
     * @param userDTOs List of {@link UserDocument} type.
     * @return Users List.
     */
    List<User> toUserList(List<UserDocument> userDTOs);

    /**
     * Creates a UserDTO from an User.
     *
     * @param user of {@link User} type.
     * @return UserDTO.
     */
    UserDocument toUserDTO(User user);

    /**
     * Creates a UserDTOs List from Users List.
     *
     * @param users List of {@link User} type.
     * @return UserDTOs List.
     */
    List<UserDocument> toUserDTOList(List<User> users);

    /**
     * Creates a Delete UserDTO from User and the deleted by whom flag.
     *
     * @param userDTO List of {@link UserDocument} type.
     * @param deletedByWhom List of {@link String} type.
     * @return UserDTO.
     */
    UserDocument toDeletedUserDTO(UserDocument userDTO, String deletedByWhom);

}

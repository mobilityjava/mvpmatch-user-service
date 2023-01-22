package com.usermanagement.app.userservice.infrastructure.user.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.usermanagement.app.userservice.domain.user.User;
import com.usermanagement.app.userservice.infrastructure.user.persistence.UserDocument;
import com.usermanagement.app.userservice.infrastructure.user.support.UserMock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserFactoryTest {

    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        this.userFactory = new UserFactoryImpl();
    }

    @DisplayName("should map UserDTO to User")
    @Test
    void shouldMapUserDTOToUser() {

        User userType = UserMock.getInstance().getUser();
        UserDocument userDTOType = UserMock.getInstance().getUserDTO();

        User actual = this.userFactory.toUser(userDTOType);

        assertNotNull(actual);
        assertEquals(userType, actual);
    }

    @DisplayName("should map UserDTO List to User List")
    @Test
    void shouldMapUserDTOListToUserList() {

        User userType = UserMock.getInstance().getUser();
        UserDocument userDTOType = UserMock.getInstance().getUserDTO();

        List<User> actual = this.userFactory.toUserList(List.of(userDTOType));

        assertNotNull(actual);
        assertEquals(userType, actual.get(0));
    }

    @DisplayName("should map User to UserDTO")
    @Test
    void shouldMapUserToUserDTO() {

        User userType = UserMock.getInstance().getUser();
        UserDocument UserDTOType = UserMock.getInstance().getUserDTO();

        UserDocument actual = this.userFactory.toUserDTO(userType);

        assertNotNull(actual);
        assertEquals(UserDTOType, actual);
    }

    @DisplayName("should map User List to UserDTO List")
    @Test
    void shouldMapUserListToUserDTOList() {

        User userType = UserMock.getInstance().getUser();
        UserDocument UserDTOType = UserMock.getInstance().getUserDTO();

        List<UserDocument> actual = this.userFactory.toUserDTOList(List.of(userType));

        assertNotNull(actual);
        assertEquals(UserDTOType, actual.get(0));
    }

    @DisplayName("should fail if UserDTO is null")
    @Test
    void shouldFailIfUserDTONull() {

        assertThrows(IllegalArgumentException.class, () -> this.userFactory.toUser(null));
    }

    @DisplayName("should fail if User is null")
    @Test
    void shouldFailIfUserNull() {

        assertThrows(NullPointerException.class, () -> this.userFactory.toUserDTO(null));
    }

}

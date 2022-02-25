package com.itransition.simpleapiserver.mock;

import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserGeneratorUtils {

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user_generator@email.com";
    private static final String USER_PASSWORD = "user_generator_password";
    private static final String USER_FIRSTNAME = "User";
    private static final String USER_LASTNAME = "Name";

    public static User generateUserModel() {
        return User.builder()
            .id(USER_ID)
            .email(USER_EMAIL)
            .password(USER_PASSWORD)
            .firstName(USER_FIRSTNAME)
            .lastName(USER_LASTNAME)
            .build();
    }

    public static UserDto generateUserDto() {
        return UserDto.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .firstname(USER_FIRSTNAME)
                .lastname(USER_LASTNAME)
                .build();
    }

    public static LoginDto generateLoginDto() {
        return LoginDto.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }
}

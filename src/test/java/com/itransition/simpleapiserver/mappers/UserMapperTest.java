package com.itransition.simpleapiserver.mappers;

import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserMapperTest {
    @Test
    public void shouldMapUserDtoToUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstname("First");
        userDto.setLastname("Last");
        userDto.setEmail("mail@test.com");
        userDto.setPassword("test_password");

        User user = UserMapper.INSTANCE.userDtoToUser(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("First");
        assertThat(user.getLastName()).isEqualTo("Last");
        assertThat(user.getEmail()).isEqualTo("mail@test.com");
        assertThat(user.getPassword()).isEqualTo("test_password");
    }

    @Test
    public void shouldMapUserToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("mail@test.com");
        user.setPassword("test_password");

        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getFirstname()).isEqualTo("First");
        assertThat(userDto.getLastname()).isEqualTo("Last");
        assertThat(userDto.getEmail()).isEqualTo("mail@test.com");
        assertThat(userDto.getPassword()).isEqualTo("test_password");
    }
}

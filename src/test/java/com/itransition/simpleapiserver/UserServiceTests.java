package com.itransition.simpleapiserver;

import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.repositories.UserRepository;
import com.itransition.simpleapiserver.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
@ActiveProfiles("test")
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private User existsUserModel;

    private UserDto existsUserDto;

    @BeforeAll
    public void setUp() {
        existsUserDto = new UserDto();
        existsUserDto.setFirstname("UserMain");
        existsUserDto.setLastname("Service");
        existsUserDto.setEmail("userMain@service.com");
        existsUserDto.setPassword("auth_main_service_password");
        User userModel = modelMapper.map(existsUserDto, User.class);
        userModel.setPassword(passwordEncoder.encode(existsUserDto.getPassword()));
        existsUserModel = userRepository.save(userModel);
    }

    @Test
    public void userGetByIdShouldNotFoundErrorResponse() {
        try {
            userService.getUserById(9999L);
            throw new IllegalAccessException();
        }
        catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(EntityNotFoundException.class);
        }
    }

    @Test
    public void userGetByIdShouldSuccessResponse() {
        User userFromService = userService.getUserById(existsUserModel.getId());
        assertThat(userFromService.getId()).isEqualTo(existsUserModel.getId());
        assertThat(userFromService.getFirstName()).isEqualTo(existsUserModel.getFirstName());
        assertThat(userFromService.getLastName()).isEqualTo(existsUserModel.getLastName());
        assertThat(userFromService.getEmail()).isEqualTo(existsUserModel.getEmail());
    }

    @Test
    public void userGetByEmailShouldNotFoundErrorResponse() {
        Optional<User> userFromService = userService.getUserByEmail("getUserMainWrongEmail@service.com");
        assertThat(userFromService.isEmpty()).isEqualTo(true);
    }

    @Test
    public void userGetByEmailShouldSuccessResponse() {
        Optional<User> userFromService = userService.getUserByEmail(existsUserModel.getEmail());
        assertThat(userFromService.get().getId()).isEqualTo(existsUserModel.getId());
        assertThat(userFromService.get().getFirstName()).isEqualTo(existsUserModel.getFirstName());
        assertThat(userFromService.get().getLastName()).isEqualTo(existsUserModel.getLastName());
        assertThat(userFromService.get().getEmail()).isEqualTo(existsUserModel.getEmail());
    }

    @Test
    public void userAuthUserShouldWrongEmailErrorResponse() {
        try {
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail("userMainWrongEmail@service.com");
            loginDto.setPassword(existsUserDto.getPassword());
            userService.authUser(loginDto);
            throw new IllegalAccessException();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("401 UNAUTHORIZED \"Email or password are incorrect\"");
        }
    }

    @Test
    public void userAuthUserShouldWrongPasswordErrorResponse() {
        try {
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(existsUserDto.getEmail());
            loginDto.setPassword("auth_main_service_wrong_password");
            userService.authUser(loginDto);
            throw new IllegalAccessException();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("401 UNAUTHORIZED \"Email or password are incorrect\"");
        }
    }

    @Test
    public void userAuthUserShouldSuccessResponse() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(existsUserDto.getEmail());
        loginDto.setPassword(existsUserDto.getPassword());
        SuccessLoginDto successLoginDto = userService.authUser(loginDto);
        assertThat(successLoginDto.getToken()).isNotEmpty();
    }

    @Test
    public void userSaveShouldDuplicateErrorResponse() {
        try {
            userService.saveUser(existsUserDto);
            throw new IllegalAccessException();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("401 UNAUTHORIZED \"Provided email already exists\"");
        }
    }

    @Test
    public void userSaveShouldSuccessResponse() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("UserSaveSuccess");
        userDto.setLastname("Service");
        userDto.setEmail("userSaveSuccess@service.com");
        userDto.setPassword("auth_save_service_password");
        User user = userService.saveUser(userDto);
        assertThat(user.getId()).isGreaterThan(existsUserModel.getId());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstname());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastname());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }
}

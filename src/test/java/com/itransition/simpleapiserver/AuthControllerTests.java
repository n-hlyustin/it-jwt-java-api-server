package com.itransition.simpleapiserver;

import com.itransition.simpleapiserver.dao.UserDao;
import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserDao userDao;

    private UserDto existsUserDto;

    @BeforeAll
    public void setUp() {
        existsUserDto = new UserDto();
        existsUserDto.setFirstname("AuthLogin");
        existsUserDto.setLastname("Controller");
        existsUserDto.setEmail("AuthLogin@controller.com");
        existsUserDto.setPassword("auth_login_controller_password");
        User user = userDao.save(existsUserDto);
        existsUserDto.setId(user.getId().intValue());
    }

    @Test
    public void loginShouldReturnWrongCredentialsErrorMessage() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("wrong@email.com");
        loginDto.setPassword("wrong_password");
        ResponseEntity<String> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(loginDto),
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void loginShouldReturnValidationErrorMessage() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("this_trash_email");
        loginDto.setPassword("wrong_password");
        ResponseEntity<String> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(loginDto),
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void loginShouldReturnSuccessMessage() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(existsUserDto.getEmail());
        loginDto.setPassword(existsUserDto.getPassword());
        ResponseEntity<SuccessLoginDto> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(loginDto),
            SuccessLoginDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getToken()).isNotEmpty();
    }

    @Test
    public void registerShouldReturnSuccessMessage() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("AuthRegister");
        userDto.setLastname("Controller");
        userDto.setEmail("AuthRegister@controller.com");
        userDto.setPassword("auth_register_controller_password");
        ResponseEntity<String> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<>(userDto),
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void registerShouldReturnValidationErrorMessage() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("AuthRegister");
        userDto.setLastname("Controller");
        userDto.setEmail("this_trash_email");
        userDto.setPassword("auth_register_controller_password");
        ResponseEntity<String> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<>(userDto),
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void registerShouldReturnAlreadyCreatedErrorMessage() {
        ResponseEntity<String> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<>(existsUserDto),
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

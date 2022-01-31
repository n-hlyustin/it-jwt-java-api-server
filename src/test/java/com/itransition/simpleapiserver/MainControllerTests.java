package com.itransition.simpleapiserver;

import static org.assertj.core.api.Assertions.assertThat;

import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.security.JwtHelper;
import com.itransition.simpleapiserver.services.UserService;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MainControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    private Long authenticatedUserId;

    @BeforeAll
    public void setUp() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("Main");
        userDto.setLastname("Controller");
        userDto.setEmail("main@controller.com");
        userDto.setPassword("main_controller_password");
        User user = userService.saveUser(userDto);
        this.authenticatedUserId = user.getId();
    }

    @Test
    public void greetingShouldReturnUnauthenticatedMessage() {
        HashMap<String, String> body = this.restTemplate.getForObject("http://localhost:" + port + "/hello", HashMap.class);
        assertThat(body.get("error")).isEqualTo("Forbidden");
    }

    @Test
    public void greetingShouldReturnAuthenticatedMessage() {
        ResponseEntity<String> response = this.restTemplate.exchange(
            "http://localhost:" + port + "/hello",
            HttpMethod.GET,
            this.getAuthenticatedHttpEntity(),
            String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello, Main Controller");
    }

    private HttpEntity getAuthenticatedHttpEntity() {
        String accessToken = jwtHelper.generateToken(this.authenticatedUserId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity(headers);
    }
}

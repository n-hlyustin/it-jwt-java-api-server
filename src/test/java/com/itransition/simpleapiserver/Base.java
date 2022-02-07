package com.itransition.simpleapiserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.simpleapiserver.configuration.MainProperties;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.mappers.UserMapper;
import com.itransition.simpleapiserver.mock.AuthRequestPostProcessor;
import com.itransition.simpleapiserver.repositories.UserRepository;
import com.itransition.simpleapiserver.security.JwtHelper;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Getter
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public abstract class Base {
    @Autowired
    private MainProperties mainProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtHelper jwtHelper;

    private UserDto existsUserDto;

    private User existsUserModel;

    @BeforeEach
    public void setUp() {
        existsUserDto = new UserDto();
        existsUserDto.setFirstname("Base");
        existsUserDto.setLastname("Test");
        existsUserDto.setEmail("base@test.com");
        existsUserDto.setPassword("base_test_password");
        User userModel = UserMapper.INSTANCE.userDtoToUser(existsUserDto);
        userModel.setPassword(passwordEncoder.encode(existsUserDto.getPassword()));
        existsUserModel = userRepository.save(userModel);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(existsUserModel);
    }

    private JwtHelper getJwtHelper() {
        if (jwtHelper == null) {
            jwtHelper = new JwtHelper(getMainProperties());
        }
        return jwtHelper;
    }

    protected AuthRequestPostProcessor auth() {
        return new AuthRequestPostProcessor(existsUserModel.getId(), getJwtHelper());
    }
}

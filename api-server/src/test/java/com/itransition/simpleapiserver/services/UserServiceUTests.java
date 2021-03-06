package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.mock.UserGeneratorUtils;
import com.itransition.simpleapiserver.repositories.UserRepository;
import com.itransition.simpleapiserver.security.JwtHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    @Test
    public void saveUserShouldThrowAnEmailAlreadyExistsError() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.saveUser(UserGeneratorUtils.generateUserDto());
        });

        verify(userRepository, only()).findByEmail(any());
        verify(passwordEncoder, never()).encode(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void saveUser() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(UserGeneratorUtils.generateUserModel());
        when(passwordEncoder.encode(any())).thenReturn("encodedpassword");
        userService.saveUser(UserGeneratorUtils.generateUserDto());

        verify(userRepository).findByEmail(any());
        verify(passwordEncoder, only()).encode(any());
        verify(userRepository).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void authUserShouldThrowAnEmailNotExistsError() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.authUser(UserGeneratorUtils.generateLoginDto());
        });

        verify(userRepository, only()).findByEmail(any());
        verify(jwtHelper, never()).generateToken(any());
        verify(request, never()).getRemoteAddr();
        verify(rabbitTemplate, never()).convertAndSend(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void authUserShouldThrowAnPasswordMissMatchError() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.authUser(UserGeneratorUtils.generateLoginDto());
        });

        verify(userRepository, only()).findByEmail(any());
        verify(passwordEncoder, only()).matches(any(), any());
        verify(jwtHelper, never()).generateToken(any());
        verify(request, never()).getRemoteAddr();
        verify(rabbitTemplate, never()).convertAndSend(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void authUser() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        userService.authUser(UserGeneratorUtils.generateLoginDto());

        verify(userRepository, only()).findByEmail(any());
        verify(passwordEncoder, only()).matches(any(), any());
        verify(jwtHelper, only()).generateToken(any());
        verify(request, only()).getRemoteAddr();
        verify(rabbitTemplate, only()).convertAndSend(anyString(), ArgumentMatchers.<Object>any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getUserByEmailShouldEmptyReturn() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        userService.getUserByEmail("test@email.com");

        verify(userRepository, only()).findByEmail(any());
    }

    @Test
    public void getUserByEmail() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        userService.getUserByEmail("test@email.com");

        verify(userRepository, only()).findByEmail(any());
    }

    @Test()
    public void getUserByIdShouldThrowAnError() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        userService.getUserById(1L);

        verify(userRepository, only()).findById(any());
    }

    @Test
    public void getUserById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        userService.getUserById(1L);

        verify(userRepository, only()).findById(any());
    }
}

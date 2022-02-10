package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.mock.UserGeneratorUtils;
import com.itransition.simpleapiserver.repositories.UserRepository;
import com.itransition.simpleapiserver.security.JwtHelper;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserRepository userRepository;

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
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void authUser() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        userService.authUser(UserGeneratorUtils.generateLoginDto());

        verify(userRepository, only()).findByEmail(any());
        verify(passwordEncoder, only()).matches(any(), any());
        verify(jwtHelper, only()).generateToken(any());
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
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        verify(userRepository, only()).findById(any());
    }

    @Test
    public void getUserById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(UserGeneratorUtils.generateUserModel()));
        userService.getUserById(1L);

        verify(userRepository, only()).findById(any());
    }
}

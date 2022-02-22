package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.configuration.RabbitMqConfiguration;
import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.mappers.UserMapper;
import com.itransition.simpleapiserver.messages.AuthLoggerMessage;
import com.itransition.simpleapiserver.repositories.UserRepository;
import com.itransition.simpleapiserver.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtHelper jwtHelper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RabbitTemplate rabbitTemplate;

    @CachePut(value = "usersCache", key = "#result.id")
    public User saveUser(UserDto userDto) {
        if (this.getUserByEmail(userDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Provided email already exists");
        }
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public SuccessLoginDto authUser(LoginDto loginDto, String ipAdress) {
        User user = getUserByEmail(loginDto.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect");
        }
        SuccessLoginDto successLoginDto = new SuccessLoginDto();
        successLoginDto.setToken(jwtHelper.generateToken(user.getId()));
        AuthLoggerMessage authLoggerMessage = new AuthLoggerMessage(user.getId(), ipAdress, Instant.now().getEpochSecond());
        rabbitTemplate.convertAndSend(RabbitMqConfiguration.AUTH_LOGGER_QUEUE_NAME, authLoggerMessage);
        return successLoginDto;
    }

    @Cacheable(value = "usersCache", key = "#p0")
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElse(null);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

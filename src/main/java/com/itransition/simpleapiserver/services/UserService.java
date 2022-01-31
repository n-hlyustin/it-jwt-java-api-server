package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.repositories.UserRepository;
import com.itransition.simpleapiserver.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtHelper jwtHelper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User saveUser(UserDto userDto) {
        if (this.getUserByEmail(userDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Provided email already exists");
        }
        User user = new User();
        user.setFirstName(userDto.getFirstname());
        user.setLastName(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public SuccessLoginDto authUser(LoginDto loginDto) {
        User user = getUserByEmail(loginDto.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect");
        }
        SuccessLoginDto successLoginDto = new SuccessLoginDto();
        successLoginDto.setToken(jwtHelper.generateToken(user.getId()));
        return successLoginDto;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException());
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

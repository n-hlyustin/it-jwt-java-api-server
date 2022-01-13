package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.dao.UserDao;
import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.security.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    private final JwtTokenRepository jwtTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto) {
        userDao.save(userDto);
    }

    public SuccessLoginDto authUser(LoginDto loginDto) {
        User user = userDao.getByEmail(loginDto.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect");
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password are incorrect");
        }
        SuccessLoginDto successLoginDto = new SuccessLoginDto();
        successLoginDto.setToken(jwtTokenRepository.generateToken(user.getId()));
        return successLoginDto;
    }

    public User getUserById(Long id) {
        return userDao.getById(id);
    }

    public User getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }
}

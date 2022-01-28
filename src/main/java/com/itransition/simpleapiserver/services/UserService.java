package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.dao.UserDao;
import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    private final JwtHelper jwtHelper;

    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto) {
        try {
            userDao.getByEmail(userDto.getEmail());
            throw new AuthenticationException("Provided email already exists");
        } catch (Exception e) {
            if (e.getClass() == EntityNotFoundException.class) {
                userDao.save(userDto);
            }
            else if (e.getClass() == AuthenticationException.class) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }
    }

    public SuccessLoginDto authUser(LoginDto loginDto) {
        User user = getUserByEmail(loginDto.getEmail());
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect");
        }
        SuccessLoginDto successLoginDto = new SuccessLoginDto();
        successLoginDto.setToken(jwtHelper.generateToken(user.getId()));
        return successLoginDto;
    }

    public User getUserById(Long id) {
        return userDao.getById(id);
    }

    public User getUserByEmail(String email) {
        try {
            return userDao.getByEmail(email);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect");
        }
    }
}

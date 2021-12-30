package com.itransition.simpleapiserver.services;

import com.itransition.simpleapiserver.dao.UserDao;
import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.security.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto) {
        userDao.save(userDto);
    }

    public String authUser(LoginDto loginDto) {
        User user = userDao.getByEmail(loginDto.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are incorrect");
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password are incorrect");
        }
        String token = jwtTokenRepository.generateToken(user.getId());
        return token;
    }

    public User getUserById(Long id) {
        return userDao.getById(id);
    }
}

package com.itransition.simpleapiserver.controllers;

import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public String register(@Validated @RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return null;
    }

    @PostMapping(path = "/login")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody String login(@Validated @RequestBody LoginDto loginDto) {
        String token = userService.authUser(loginDto);
        return token;
    }
}

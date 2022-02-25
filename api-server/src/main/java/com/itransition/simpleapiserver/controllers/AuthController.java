package com.itransition.simpleapiserver.controllers;

import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.SuccessLoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/register")
    public void register(@Validated @RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

    @PostMapping(path = "/login")
    public SuccessLoginDto login(@Validated @RequestBody LoginDto loginDto) {
        return userService.authUser(loginDto);
    }
}

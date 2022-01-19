package com.itransition.simpleapiserver.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @NotNull(message = "Email must not be empty")
    @Email(message = "Please provide correct email address")
    private String email;

    @NotNull(message = "Password must not be empty")
    @Size(min = 4, message = "Password length must be more 4 characters")
    private String password;
}

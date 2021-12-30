package com.itransition.simpleapiserver.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class UserDto {

    private Integer id;

    @NotNull(message = "Firstname must not be empty")
    @Size(min = 2, message = "Firstname length must be more 2 characters")
    private String firstname;

    private String lastname;

    @NotNull(message = "Email must not be empty")
    @Email(message = "Please provide correct email address")
    private String email;

    @NotNull(message = "Password must not be empty")
    @Size(min = 4, message = "Password length must be more 4 characters")
    private String password;
}

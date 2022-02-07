package com.itransition.simpleapiserver;

import com.itransition.simpleapiserver.dto.LoginDto;
import com.itransition.simpleapiserver.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTests extends Base {
    @Test
    public void loginShouldReturnWrongCredentialsErrorMessage() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("wrong@email.com");
        loginDto.setPassword("wrong_password");
        getMockMvc().perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(getObjectMapper().writeValueAsString(loginDto))
        )
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginShouldReturnValidationErrorMessage() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("this_trash_email");
        loginDto.setPassword("wrong_password");
        getMockMvc().perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(getObjectMapper().writeValueAsString(loginDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginShouldReturnSuccessMessage() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(getExistsUserDto().getEmail());
        loginDto.setPassword(getExistsUserDto().getPassword());
        getMockMvc().perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(getObjectMapper().writeValueAsString(loginDto))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void registerShouldReturnSuccessMessage() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstname("AuthRegister");
        userDto.setLastname("Controller");
        userDto.setEmail("AuthRegister@controller.com");
        userDto.setPassword("auth_register_controller_password");
        getMockMvc().perform(
            post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(getObjectMapper().writeValueAsString(userDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void registerShouldReturnValidationErrorMessage() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstname("AuthRegister");
        userDto.setLastname("Controller");
        userDto.setEmail("this_trash_email");
        userDto.setPassword("auth_register_controller_password");
        getMockMvc().perform(
            post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(getObjectMapper().writeValueAsString(userDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void registerShouldReturnAlreadyCreatedErrorMessage() throws Exception {
        getMockMvc().perform(
            post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(getObjectMapper().writeValueAsString(getExistsUserDto()))
        )
            .andExpect(status().isUnauthorized());
    }
}

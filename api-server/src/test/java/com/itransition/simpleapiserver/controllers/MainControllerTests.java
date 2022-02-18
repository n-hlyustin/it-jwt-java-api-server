package com.itransition.simpleapiserver.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

public class MainControllerTests extends Base {
    @Test
    public void greetingShouldReturnUnauthenticatedMessage() throws Exception {
        getMockMvc().perform(get("/hello")).andExpect(status().isForbidden());
    }

    @Test
    public void greetingShouldReturnAuthenticatedMessage() throws Exception {
        getMockMvc().perform(
            get("/hello")
            .with(auth())
        )
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("Hello, Base Test")));
    }
}

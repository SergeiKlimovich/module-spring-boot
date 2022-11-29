package com.javaprogram.modulespringboot.web;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaprogram.modulespringboot.model.User;
import com.javaprogram.modulespringboot.service.UserService;
import com.javaprogram.modulespringboot.service.dto.UserDto;

import lombok.SneakyThrows;

@AutoConfigureMockMvc
@ContextConfiguration(classes = UserRestControllerV1.class)
@WebMvcTest
public class UserRestControllerV1Test {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SneakyThrows
    private String mapToJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @SneakyThrows
    void shouldFindUserWhenExistingIdPassed() {
        when(userService.findById(1L)).thenReturn(Optional.of(new UserDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1")
                        .with(user("username"))
                        .with(csrf())
                        .content(String.valueOf(new User()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @SneakyThrows
    void shouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("TestFirstName");
        userDto.setLastName("TestLastName");

        UserDto expected = new UserDto();
        expected.setFirstName("TestFirstName");
        expected.setLastName("TestLastName");

        when(userService.register(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration")
                        .with(user("username"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(expected)))
                .andExpect(status().isCreated())
                .andReturn();
    }
}

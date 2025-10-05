package com.khanhjava.khanh_learn_java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanhjava.khanh_learn_java.dto.request.UserCreationRequest;
import com.khanhjava.khanh_learn_java.dto.response.UserResponse;
import com.khanhjava.khanh_learn_java.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse response;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(2000, 8, 7);
        request = UserCreationRequest.builder()
                                     .username("KhanhAdmin")
                                     .firstName("khanh")
                                     .lastName("Luu")
                                     .password("123123")
                                     .dateOfBirth(dob)
                                     .build();

        response = UserResponse.builder()
                               .id("123123123")
                               .username("KhanhAdmin")
                               .firstName("khanh")
                               .lastName("Luu")
                               .dateOfBirth(dob)
                               .build();
    }

    @Test
    void createUser() throws Exception {
        // GIVEN
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);

        // WHEN, THEN
        mockMvc.perform(
                       MockMvcRequestBuilders
                               .post("/users")
                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                               .content(content)
               )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.CREATED.value()));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setUsername("kh");
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);

        // WHEN, THEN
        mockMvc.perform(
                       MockMvcRequestBuilders
                               .post("/users")
                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                               .content(content)
               )
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(1002))
               .andExpect(MockMvcResultMatchers.jsonPath("message")
                                               .value("Username must be at least 3 characters long"));
    }
}


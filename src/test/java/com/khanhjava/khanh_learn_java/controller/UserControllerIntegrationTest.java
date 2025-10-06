package com.khanhjava.khanh_learn_java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanhjava.khanh_learn_java.dto.request.UserCreationRequest;
import com.khanhjava.khanh_learn_java.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.38");
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private UserCreationRequest request;
    private UserResponse response;

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

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
}


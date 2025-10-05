package com.khanhjava.khanh_learn_java.service;

import com.khanhjava.khanh_learn_java.dto.request.UserCreationRequest;
import com.khanhjava.khanh_learn_java.dto.response.UserResponse;
import com.khanhjava.khanh_learn_java.entity.User;
import com.khanhjava.khanh_learn_java.exception.AppException;
import com.khanhjava.khanh_learn_java.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse response;
    private User user;

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

        user = User.builder()
                   .id("123123123")
                   .username("KhanhAdmin")
                   .firstName("khanh")
                   .lastName("Luu")
                   .dateOfBirth(dob).build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        // WHEN
        UserResponse response = userService.createUser(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo(user.getId());
    }

    @Test
    void createUser_validRequest_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);
        
        // THEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }
}

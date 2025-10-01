package com.khanhjava.khanh_learn_java.controller;

import com.khanhjava.khanh_learn_java.dto.request.UserCreationRequest;
import com.khanhjava.khanh_learn_java.dto.request.UserUpdateRequest;
import com.khanhjava.khanh_learn_java.dto.response.ApiResponse;
import com.khanhjava.khanh_learn_java.dto.response.UserResponse;
import com.khanhjava.khanh_learn_java.entity.User;
import com.khanhjava.khanh_learn_java.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity(jsr250Enabled = true)
public class UserController {
    UserService userService;

    @PostMapping
    ResponseEntity<ApiResponse<UserResponse>> createUser(
            @RequestBody @Valid UserCreationRequest request,
            HttpServletRequest httpServletRequest) {
        return ApiResponse.success(
                userService.createUser(request),
                "Create user successfully",
                HttpStatus.CREATED.value(),
                httpServletRequest.getRequestURI()
        );
    }

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('APPROVE_POST')")
    ResponseEntity<Object> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Username: " + username);
        authentication.getAuthorities().forEach(System.out::println);
        return ResponseEntity.ok(Map.of(
                "username", username,
                "data", userService.getUsers()
        ));
    }

    @PostAuthorize("hasRole('ADMIN') or returnObject.username == authentication.username")
    @GetMapping("/{userId}")
    User getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@RequestBody UserUpdateRequest request, @PathVariable String userId) {
        return userService.updateUser(request, userId);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return userId;
    }

    @GetMapping("/me")
    ResponseEntity<ApiResponse<UserResponse>> getInfo(
            HttpServletRequest httpServletRequest) {
        return ApiResponse.success(
                userService.getMeInfo(),
                "get info me successfully",
                HttpStatus.OK.value(),
                httpServletRequest.getRequestURI()
        );
    }
}

package com.khanhjava.khanh_learn_java.controller;

import com.khanhjava.khanh_learn_java.dto.request.IntrospectRequest;
import com.khanhjava.khanh_learn_java.dto.request.LoginRequest;
import com.khanhjava.khanh_learn_java.dto.request.RefreshTokenRequest;
import com.khanhjava.khanh_learn_java.dto.response.ApiResponse;
import com.khanhjava.khanh_learn_java.dto.response.AuthResponse;
import com.khanhjava.khanh_learn_java.dto.response.IntrospectResponse;
import com.khanhjava.khanh_learn_java.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    HttpServletRequest httpServletRequest;

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success(authenticationService.login(request),
                                   "Login successfully",
                                   HttpStatus.OK.value(),
                                   httpServletRequest.getRequestURI()
        );
    }

    @PostMapping("/hehe/logout")
    ResponseEntity<ApiResponse<Boolean>> logout() throws ParseException, JOSEException {
        String token = httpServletRequest.getHeader("Authorization");
        return ApiResponse.success(authenticationService.logout(token),
                                   "Logout successfully",
                                   HttpStatus.OK.value(),
                                   httpServletRequest.getRequestURI()
        );
    }

    @PostMapping("/introspect")
    ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request) throws
            ParseException,
            JOSEException {
        return ApiResponse.success(authenticationService.introspect(request),
                                   "Introspect successfully",
                                   HttpStatus.OK.value(),
                                   httpServletRequest.getRequestURI()
        );
    }

    @PostMapping("/refresh-token")
    ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) throws
            ParseException,
            JOSEException {
        return ApiResponse.success(authenticationService.refreshToken(request),
                                   "Refresh token successfully",
                                   HttpStatus.OK.value(),
                                   httpServletRequest.getRequestURI()
        );
    }

}

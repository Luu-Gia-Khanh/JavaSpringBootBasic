package com.khanhjava.khanh_learn_java.controller;

import com.khanhjava.khanh_learn_java.dto.request.PermissionRequest;
import com.khanhjava.khanh_learn_java.dto.response.ApiResponse;
import com.khanhjava.khanh_learn_java.dto.response.PermissionResponse;
import com.khanhjava.khanh_learn_java.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @RequestBody @Valid PermissionRequest request,
            HttpServletRequest httpServletRequest) {
        return ApiResponse.success(
                permissionService.create(request),
                "Create permission successfully",
                HttpStatus.CREATED.value(),
                httpServletRequest.getRequestURI()
        );
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermission(HttpServletRequest httpServletRequest) {
        return ApiResponse.success(
                permissionService.getAll(),
                "Get permission successfully",
                HttpStatus.OK.value(),
                httpServletRequest.getRequestURI()
        );
    }

    @DeleteMapping("/{name}")
    ResponseEntity<ApiResponse<Void>> deletePermission(
            @PathVariable String name,
            HttpServletRequest httpServletRequest) {
        permissionService.delete(name);
        return ApiResponse.success(
                null,
                "Delete permission successfully",
                HttpStatus.OK.value(),
                httpServletRequest.getRequestURI()
        );
    }
}

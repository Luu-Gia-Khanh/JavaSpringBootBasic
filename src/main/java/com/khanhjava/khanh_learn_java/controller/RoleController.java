package com.khanhjava.khanh_learn_java.controller;

import com.khanhjava.khanh_learn_java.dto.request.RoleRequest;
import com.khanhjava.khanh_learn_java.dto.response.ApiResponse;
import com.khanhjava.khanh_learn_java.dto.response.RoleResponse;
import com.khanhjava.khanh_learn_java.service.RoleService;
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
@RequestMapping("/roles")
public class RoleController extends BaseController<RoleResponse, RoleRequest> {
    RoleService roleService;

    @PostMapping
    @Override
    ResponseEntity<ApiResponse<RoleResponse>> create(
            @RequestBody @Valid RoleRequest request,
            HttpServletRequest httpServletRequest) {
        return ApiResponse.success(
                roleService.create(request),
                "Create role successfully",
                HttpStatus.CREATED.value(),
                httpServletRequest.getRequestURI()
        );
    }

    @GetMapping
    @Override
    ResponseEntity<ApiResponse<List<RoleResponse>>> getAll(HttpServletRequest httpServletRequest) {
        return ApiResponse.success(
                roleService.getAll(),
                "Get role successfully",
                HttpStatus.OK.value(),
                httpServletRequest.getRequestURI()
        );
    }

    @DeleteMapping("/{id}")
    @Override
    ResponseEntity<ApiResponse<Void>> delete(String id, HttpServletRequest httpServletRequest) {
        roleService.delete(id);
        return ApiResponse.success(
                null,
                "Delete role successfully",
                HttpStatus.OK.value(),
                httpServletRequest.getRequestURI()
        );
    }
}

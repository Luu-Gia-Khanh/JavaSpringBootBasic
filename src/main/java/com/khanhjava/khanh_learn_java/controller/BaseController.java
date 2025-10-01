package com.khanhjava.khanh_learn_java.controller;

import com.khanhjava.khanh_learn_java.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class BaseController<T, K> {
    abstract ResponseEntity<ApiResponse<T>> create(
            @RequestBody @Valid K request,
            HttpServletRequest httpServletRequest);

    abstract ResponseEntity<ApiResponse<List<T>>> getAll(HttpServletRequest httpServletRequest);

    abstract ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id, HttpServletRequest httpServletRequest);

}

package com.khanhjava.khanh_learn_java.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int statusCode;
    private String path;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, int statusCode, String path) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                                                .success(true)
                                                .message(message)
                                                .data(data)
                                                .statusCode(statusCode)
                                                .path(path)
                                                .timestamp(LocalDateTime.now())
                                                .build();
        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            String message,
            int code,
            HttpStatusCode statusCode,
            String path) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                                                .success(false)
                                                .message(message)
                                                .statusCode(code)
                                                .path(path)
                                                .timestamp(LocalDateTime.now())
                                                .build();
        return ResponseEntity.status(statusCode).body(apiResponse);
    }
}

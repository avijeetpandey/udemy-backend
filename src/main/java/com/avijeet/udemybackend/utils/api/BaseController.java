package com.avijeet.udemybackend.utils.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected <T>ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return new ResponseEntity<>(ApiResponse.success(data, message), HttpStatus.OK);
    }
}

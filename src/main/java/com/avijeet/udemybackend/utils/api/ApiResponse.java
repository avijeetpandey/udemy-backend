package com.avijeet.udemybackend.utils.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    @JsonProperty("isError")
    private final Boolean isError;
    private final T data;
    private final  String message;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(false, data, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(true, null, message);
    }
}

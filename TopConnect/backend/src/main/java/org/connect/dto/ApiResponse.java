package org.connect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private T data;
    private int code;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.code = 200;
    }
}


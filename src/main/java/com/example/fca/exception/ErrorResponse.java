package com.example.fca.exception;
// exception/ErrorResponse.java

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;
}

package com.rakeshv.catalogservice.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExceptionResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private String message;
    private HttpStatus status;

    public static ExceptionResponse of(String message, HttpStatus status) {
        return new ExceptionResponse(message, status);
    }
}
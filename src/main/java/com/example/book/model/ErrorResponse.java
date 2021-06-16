package com.example.book.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Error response object that is returned to clients in case of errors
 */
@Getter
@Setter
public class ErrorResponse {

    private String message;
}

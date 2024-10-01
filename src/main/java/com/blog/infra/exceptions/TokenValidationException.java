package com.blog.infra.exceptions;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

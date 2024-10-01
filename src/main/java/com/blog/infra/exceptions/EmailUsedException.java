package com.blog.infra.exceptions;

public class EmailUsedException extends RuntimeException {

    public EmailUsedException(String message) {
        super(message);
    }
}

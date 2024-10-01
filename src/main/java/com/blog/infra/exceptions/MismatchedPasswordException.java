package com.blog.infra.exceptions;

public class MismatchedPasswordException extends RuntimeException {

    public MismatchedPasswordException(String message) {
        super(message);
    }
}

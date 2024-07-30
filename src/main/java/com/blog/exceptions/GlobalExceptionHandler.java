package com.blog.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return generateProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmailUsedException.class)
    public ResponseEntity<ProblemDetail> emailUsedExceptionHandler(EmailUsedException ex) {
        return generateProblemDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MismatchedPasswordException.class)
    public ResponseEntity<ProblemDetail> mismatchedPasswordExceptionHandler(MismatchedPasswordException ex) {
        return generateProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> badCredentialsExceptionHandler(BadCredentialsException ex) {
        return this.generateProblemDetail(HttpStatus.BAD_REQUEST, "Email ou senha incorretos.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return this.generateProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> illegalStateExceptionHandler(IllegalStateException ex) {
        return this.generateProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> runtimeExceptionHandler(RuntimeException ex) {
        return generateProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro no servidor, contate os desenvolvedores.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> fieldsMessage = new ArrayList<>();

        fieldErrors.forEach(fieldError -> fieldsMessage.add(fieldError.getDefaultMessage()));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Campo(s) não preenchidos corretamente.");
        problemDetail.setProperty("fieldsMessage", fieldsMessage);
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    private ResponseEntity<ProblemDetail> generateProblemDetail(HttpStatus httpStatus, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }
}

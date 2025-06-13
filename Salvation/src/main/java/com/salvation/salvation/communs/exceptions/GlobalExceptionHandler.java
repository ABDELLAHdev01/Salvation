package com.salvation.salvation.communs.exceptions;


import com.salvation.salvation.communs.response.ErrorResponseFormat;
import com.salvation.salvation.communs.response.ErrorResponseSimpleFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ErrorResponseFormat errorResponse;

    @Autowired
    private ErrorResponseSimpleFormat errorSimpleResponse;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseFormat> handleValidationException(
            MethodArgumentNotValidException exception, HttpServletRequest request) {


        Map<String, List<String>> errorDetails = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                ));

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage("Validation Failed");
        errorResponse.setDetails(errorDetails);
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleNoSuchElementException(
            NoSuchElementException exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("Resource Not Found");
        errorSimpleResponse.setDetails(Collections.singletonList(exception.getMessage()));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorSimpleResponse);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleNoSuchFileException(
            NoSuchFileException exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("File Not Found");
        errorSimpleResponse.setDetails(Collections.singletonList(exception.getMessage()));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorSimpleResponse);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("Data Integrity Violation");
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorSimpleResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleIllegalArgumentException(
            IllegalArgumentException exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("Illegal Argument Exception");
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorSimpleResponse);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleUsernameAlreadyExistsException(
            UsernameAlreadyExistsException exception, HttpServletRequest request) {
        errorSimpleResponse.setMessage("Username already exists");
        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorSimpleResponse);
    }
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException exception, HttpServletRequest request) {
        errorSimpleResponse.setMessage("Invalid refresh token");
        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorSimpleResponse);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleLockedException(
            LockedException exception, HttpServletRequest request) {
        errorSimpleResponse.setMessage("User account is locked");
        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setDetails(Collections.singletonList(
                "Your account has been locked for security reasons. Please contact support for assistance."
        ));
        errorSimpleResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorSimpleResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleGenericException(
            Exception exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("Internal Server Error");
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorSimpleResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleResourceNotFoundException(
            ResourceNotFoundException exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("Resource Not Found");
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorSimpleResponse);
    }

    @ExceptionHandler(UserAlreadyHasCharacter.class)
    public ResponseEntity<ErrorResponseSimpleFormat> handleUserAlreadyHasCharacter(
            UserAlreadyHasCharacter exception, HttpServletRequest request) {

        errorSimpleResponse.setTimestamp(LocalDateTime.now());
        errorSimpleResponse.setMessage("User Already Has Character");
        errorSimpleResponse.setDetails(Arrays.asList(exception.getMessage().split(";")));
        errorSimpleResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorSimpleResponse);
    }

}

package edu.java.scrapper.controller.advice;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class BadRequestControllerAdvice {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        var body = buildResponse(e, "Header '" + e.getHeaderName() + "' is missing");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ApiErrorResponse buildResponse(Exception e, String description) {
        return ApiErrorResponse.builder()
            .description(description)
            .code(HttpStatus.BAD_REQUEST.toString())
            .exceptionName(e.getClass().getName())
            .exceptionMessage(e.getMessage())
            .stacktrace(Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList())
            .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e
    ) {
        var body = buildResponse(e, "Parameter '" + e.getPropertyName() + "' is not valid");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception that is thrown when validating values with Bean-validation.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> violations = new HashMap<>();
        for (var violation : e.getConstraintViolations()) {
            PathImpl path = (PathImpl) violation.getPropertyPath();
            violations.put(path.getLeafNode().getName(), violation.getMessage());
        }

        var body = buildResponse(e, getDescription(violations));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private String getDescription(Map<String, String> violations) {
        StringBuilder description = new StringBuilder("Constraint violation\n");
        for (var entry : violations.entrySet()) {
            description.append(entry.getKey());
            description.append(" - ");
            description.append(entry.getValue());
            description.append('\n');
        }
        return description.toString();
    }

    /**
     * Handles the exception that is thrown when validating request DTO using @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> violations = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            violations.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        var body = buildResponse(e, getDescription(violations));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

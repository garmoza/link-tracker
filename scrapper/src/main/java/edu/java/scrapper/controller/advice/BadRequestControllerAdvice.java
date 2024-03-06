package edu.java.scrapper.controller.advice;

import edu.java.model.response.ApiErrorResponse;
import edu.java.model.util.ApiErrorResponses;
import edu.java.model.util.ConstraintViolationFormatter;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class BadRequestControllerAdvice {

    private static final int BAD_REQUEST_CODE = 400;

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        var body = ApiErrorResponses.of(e, BAD_REQUEST_CODE, "Header '" + e.getHeaderName() + "' is missing");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e
    ) {
        var body = ApiErrorResponses.of(e, BAD_REQUEST_CODE, "Parameter '" + e.getPropertyName() + "' is not valid");
        return ResponseEntity.badRequest().body(body);
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
        String description = ConstraintViolationFormatter.getDescription(violations);

        var body = ApiErrorResponses.of(e, BAD_REQUEST_CODE, description);
        return ResponseEntity.badRequest().body(body);
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
        String description = ConstraintViolationFormatter.getDescription(violations);

        var body = ApiErrorResponses.of(e, BAD_REQUEST_CODE, description);
        return ResponseEntity.badRequest().body(body);
    }
}

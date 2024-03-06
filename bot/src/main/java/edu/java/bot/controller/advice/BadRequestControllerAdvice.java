package edu.java.bot.controller.advice;

import edu.java.model.response.ApiErrorResponse;
import edu.java.model.util.ApiErrorResponses;
import edu.java.model.util.ConstraintViolationFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BadRequestControllerAdvice {

    private static final int BAD_REQUEST_CODE = 400;

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

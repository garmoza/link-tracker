package edu.java.scrapper.controller.advice;

import edu.java.model.response.ApiErrorResponse;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundControllerAdvice {

    @ExceptionHandler({
        LinkNotFoundException.class,
        TgChatNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(RuntimeException e) {
        var body = buildResponse(e, "Entity not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    private ApiErrorResponse buildResponse(Exception e, String description) {
        return ApiErrorResponse.builder()
            .description(description)
            .code(HttpStatus.NOT_FOUND.toString())
            .exceptionName(e.getClass().getName())
            .exceptionMessage(e.getMessage())
            .stacktrace(Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList())
            .build();
    }
}

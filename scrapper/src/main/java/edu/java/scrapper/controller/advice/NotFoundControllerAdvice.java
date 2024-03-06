package edu.java.scrapper.controller.advice;

import edu.java.model.response.ApiErrorResponse;
import edu.java.model.util.ApiErrorResponses;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundControllerAdvice {

    private static final int NOT_FOUND_CODE = 404;

    @ExceptionHandler({
        LinkNotFoundException.class,
        TgChatNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(RuntimeException e) {
        var body = ApiErrorResponses.of(e, NOT_FOUND_CODE, "Entity not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

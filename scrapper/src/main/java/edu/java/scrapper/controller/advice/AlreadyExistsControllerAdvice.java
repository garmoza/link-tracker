package edu.java.scrapper.controller.advice;

import edu.java.scrapper.exception.LinkAlreadyExistsException;
import edu.java.scrapper.exception.TgChatAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AlreadyExistsControllerAdvice {

    @ExceptionHandler({
        LinkAlreadyExistsException.class,
        TgChatAlreadyExistsException.class
    })
    public ResponseEntity<Void> handleAlreadyExistsException(RuntimeException e) {
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }
}

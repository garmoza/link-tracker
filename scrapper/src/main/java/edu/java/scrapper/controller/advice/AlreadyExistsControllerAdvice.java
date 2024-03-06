package edu.java.scrapper.controller.advice;

import edu.java.model.response.LinkResponse;
import edu.java.scrapper.exception.LinkAlreadyExistsException;
import edu.java.scrapper.exception.TgChatAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AlreadyExistsControllerAdvice {

    @ExceptionHandler(TgChatAlreadyExistsException.class)
    public ResponseEntity<Void> handleTgChatAlreadyExistsException() {
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    public ResponseEntity<LinkResponse> handleLinkAlreadyExistsException(LinkAlreadyExistsException e) {
        return new ResponseEntity<>(e.getLink(), HttpStatus.ALREADY_REPORTED);
    }
}

package com.example.sports.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handlerArgumentException() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/game");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body("NOT FOUND");
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity handlerNoSuchElement() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/game");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .body("NOT FOUND");
    }
}

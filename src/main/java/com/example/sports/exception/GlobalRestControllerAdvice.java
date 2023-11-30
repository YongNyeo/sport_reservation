package com.example.sports.exception;

import com.example.sports.exception.custom.MinimumAmountTickets;
import com.example.sports.exception.custom.NotEnoughSeat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler()
    ResponseEntity handlerArgumentException(MethodArgumentNotValidException methodArgumentNotValidException) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("값을 잘못 입력하셨습니다.");
    }

    @ExceptionHandler()
    ResponseEntity handlerNoSuchElement(NoSuchElementException noSuchElementException) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/game");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .body("NOT FOUND");
    }

    @ExceptionHandler()
    ResponseEntity handlerMinimumAmount(MinimumAmountTickets minimumAmountTickets) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(minimumAmountTickets.getMessage());
    }
    @ExceptionHandler()
    ResponseEntity handlerNotEnoughSeat(NotEnoughSeat notEnoughSeat) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(notEnoughSeat.getMessage());
    }
}

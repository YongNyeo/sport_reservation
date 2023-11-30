package com.example.sports.exception.custom;

public class MinimumAmountTickets extends RuntimeException{
    private final  String message = "티켓의 최소 구매 수량은  1개 이상입니다. ";

    @Override
    public String getMessage() {
        return message;
    }
}

package com.example.sports.exception.custom;

public class NotEnoughSeat extends RuntimeException{
    private final String message = "잔여 좌석을 초과하였습니다.";
}

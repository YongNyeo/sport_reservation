package com.example.sports.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class RequestReservationDto {
    @NotNull
    private final UUID gameId;

    @NotBlank
    private final String username;

    @NotBlank
    private final String email;

    @NotNull
    private final int adultNumber;

    @NotNull
    private final int minorNumber;

    public UUID getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAdultNumber() {
        return adultNumber;
    }

    public int getMinorNumber() {
        return minorNumber;
    }

}

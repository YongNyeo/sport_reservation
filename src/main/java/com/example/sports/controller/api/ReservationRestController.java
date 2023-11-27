package com.example.sports.controller.api;

import com.example.sports.domain.reservation.Reservation;
import com.example.sports.domain.reservation.dto.RequestReservationDto;

import com.example.sports.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {


    private final ReservationService reservationService;

    @PostMapping("")
    ResponseEntity<Reservation> create(@RequestBody @Validated RequestReservationDto dto ) {

        Reservation reservation = reservationService.save(dto);

        URI location = URI.create("/reservation/" + reservation.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable UUID id) {
        reservationService.deleteById(id);

        URI location = URI.create("/reservation");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(headers)
                .build();
    }
}

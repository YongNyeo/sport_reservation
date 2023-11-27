package com.example.sports.controller;

import com.example.sports.domain.reservation.Reservation;
import com.example.sports.domain.ticket.Ticket;
import com.example.sports.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
    String getReservations(Model model) {
        List<Reservation> reservations = reservationService.findAll();
        model.addAttribute("reservations", reservations);
        return "reservation_list.html";
    }

    @GetMapping("/{id}")
    String getTickets(@PathVariable UUID id, Model model) {
        Reservation reservation = reservationService.findById(id);
        List<Ticket> tickets = reservation.getTickets();
        model.addAttribute("tickets", tickets);
        return "reservation_one.html";
    }
}

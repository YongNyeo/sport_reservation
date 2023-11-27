package com.example.sports.service;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.reservation.Reservation;
import com.example.sports.domain.reservation.dto.RequestReservationDto;
import com.example.sports.domain.ticket.Ticket;
import com.example.sports.exception.custom.MinimumAmountTickets;
import com.example.sports.exception.custom.NotEnoughSeat;
import com.example.sports.repository.GameRepository;
import com.example.sports.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final GameRepository gameRepository;

    public Reservation save(RequestReservationDto dto) {


        if (!Reservation.validMinimumTickets(dto.getMinorNumber(),dto.getAdultNumber())) {
            throw new MinimumAmountTickets();
        }
        Game game = gameRepository.findById(dto.getGameId()).orElseThrow(NoSuchElementException::new);

        if (!game.isEnoughSeat(dto.getAdultNumber() + dto.getMinorNumber())) {
            throw new NotEnoughSeat();
        }

        game.minusSeat(dto.getAdultNumber()+ dto.getMinorNumber());

        Reservation reservation = new Reservation();
        reservation.createReservation(dto);

        Ticket.createTickets(dto, game, reservation);

        return reservationRepository.save(reservation);

    }

    @Transactional(readOnly = true)
    public Reservation findById(UUID id) {
        return reservationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteById(UUID id) {

        Reservation reservation = reservationRepository.findById(id).orElseThrow(NoSuchElementException::new);
        List<Ticket>tickets = reservation.getTickets();

        tickets.get(0).getGame().plusSeat(tickets.size());

        reservationRepository.deleteById(id);
    }
}

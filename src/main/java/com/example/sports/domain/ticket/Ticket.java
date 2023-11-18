package com.example.sports.domain.ticket;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.reservation.Reservation;
import com.example.sports.domain.reservation.dto.RequestReservationDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TicketType ticketType;

    private int price;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Game game;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static List<Ticket> createTickets(RequestReservationDto dto, Game game) {
        List<Ticket> tickets = new ArrayList<>();
        int adult = dto.getAdultNumber();
        int minor = dto.getMinorNumber();
        for (int i = 0; i < adult; i++) {
            tickets.add(Ticket.builder()
                    .price(10000)
                    .ticketType(TicketType.ADULT)
                    .game(game)
                    .build());
        }
        for (int i = 0; i < minor; i++) {
            tickets.add(Ticket.builder()
                    .price(5000)
                    .ticketType(TicketType.MINOR)
                    .game(game)
                    .build());
        }
        return tickets;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}

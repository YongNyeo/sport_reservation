package com.example.sports.domain.reservation;

import com.example.sports.domain.reservation.dto.RequestReservationDto;
import com.example.sports.domain.ticket.Ticket;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Length(max = 10)
    private String username;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Long totalSpending;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public void createReservation(RequestReservationDto dto, List<Ticket> tickets) {

        Long totalSpending = (dto.getAdultNumber() * 10000L) + (dto.getMinorNumber() * 5000L);

        this.email = dto.getEmail();
        this.username = dto.getUsername();
        this.status = PaymentStatus.WAITING;
        this.totalSpending = totalSpending;
        this.tickets = tickets;

        tickets.forEach((ticket) -> ticket.setReservation(this));

    }

    public static boolean validMinimumTickets(int adultNumber,int minorNumber) {
        return adultNumber != 0 || minorNumber != 0;
    }
}

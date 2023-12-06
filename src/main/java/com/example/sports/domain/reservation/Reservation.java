package com.example.sports.domain.reservation;

import com.example.sports.domain.game.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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

    private String email;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private int adultNumber;

    private int minorNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    //private User user;

    public boolean validMinimumTickets() {
        return this.adultNumber != 0 || this.minorNumber != 0;
    }

    public void completePaymentStatus() {
        status = PaymentStatus.COMPLETE;
    }

//    public void changeTickets(List<Ticket>tickets) {
//        this.tickets = tickets;
//    }
}

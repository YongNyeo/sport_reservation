package com.example.sports.domain.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Game {

    public Game() {
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    private String team1;

    private String team2;

    private int seat;

    private int price;


    private String dateTime;

    private String location;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public boolean isEnoughSeat(int sum) {
        return sum <= seat;
    }

    public void minusSeat(int sum) {
        this.seat -= sum;
    }

    public void plusSeat(int sum) {
        this.seat += sum;
    }
}

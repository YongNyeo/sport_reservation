package com.example.sports.domain.game.dto;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.GameType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
public class RequestGameDto { //updateDto = saveDto

    @NotBlank
    private final String team1;

    @NotBlank
    private final String team2;

    @NotBlank
    private final String gameDate;

    @NotNull
    @Range(min = 1000,max = 99999)
    private final int seat;

    @NotNull
    @Range(min= 0,max =  999999)
    private final int price;
    @NotBlank
    private final String location;
    @NotNull
    private final GameType gameType;

    public static Game toEntity(RequestGameDto dto) {
        return Game.builder()
                .gameType(dto.getGameType())
                .team1(dto.getTeam1())
                .seat(dto.getSeat())
                .price(dto.getPrice())
                .team2(dto.getTeam2())
                .location(dto.getLocation())
                .dateTime(dto.getGameDate())
                .build();
    }
}

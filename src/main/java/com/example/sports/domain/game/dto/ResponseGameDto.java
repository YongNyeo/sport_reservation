package com.example.sports.domain.game.dto;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.GameType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ResponseGameDto {

    private final UUID id;
    private final String team1;
    private final String team2;
    private final int seat;
    private final int price;
    private final String gameDate;
    private final String location;
    private final GameType gameType;

    public static ResponseGameDto toDto(Game entity) {
        return ResponseGameDto.builder()
                .id(entity.getId())
                .gameType(entity.getGameType())
                .team1(entity.getTeam1())
                .team2(entity.getTeam2())
                .seat(entity.getSeat())
                .price(entity.getPrice())
                .location(entity.getLocation())
                .gameDate(entity.getDateTime())
                .build();

    }
}

package com.example.sports.controller.api;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.dto.RequestGameDto;
import com.example.sports.domain.game.dto.ResponseGameDto;
import com.example.sports.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameRestController {
    private final GameService gameService;

    @PostMapping("")
    ResponseEntity<ResponseGameDto> create(@RequestBody @Validated RequestGameDto dto) {

        Game game = gameService.save(dto);

        URI location = URI.create("/game/" + game.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(ResponseGameDto.toDto(game));
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable UUID id) {
        gameService.deleteById(id);

        URI location = URI.create("/game");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(headers)
                .build();
    }
}

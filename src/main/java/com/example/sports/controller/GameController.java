package com.example.sports.controller;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.dto.ResponseGameDto;
import com.example.sports.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequestMapping("/game")
@Controller
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("")
    String getGameList(Model model) {
        List<Game> games = gameService.findAll();
        model.addAttribute("games", games.stream()
                .map(game -> ResponseGameDto.toDto(game))
                .collect(Collectors.toUnmodifiableList()));
        return "game_list.html";
    }

    @GetMapping("/{id}")
        //->상세 페이지에서 바로 예매 화면 시작
    String getGameById(@PathVariable UUID id, Model model) {
        Game game = gameService.findById(id);
        model.addAttribute("game", ResponseGameDto.toDto(game));
        return "game_one.html";
    }

    @GetMapping("/form")
    String getGameForm() {
        return "game_form.html";
    }
}

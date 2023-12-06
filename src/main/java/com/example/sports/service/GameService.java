package com.example.sports.service;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.dto.RequestGameDto;
import com.example.sports.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService { //service 는 도메인 검증과 같은 핵심 비즈니스로직만 포함한다.

    private final GameRepository gameRepository;

    public Game save(RequestGameDto dto) {
        Game game = RequestGameDto.toEntity(dto);

        //game 생성할때 좌석도 생성을 해야함.

        return gameRepository.save(game);
    }

    @Transactional(readOnly = true)
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Game findById(UUID id) {

        return gameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 경기 정보가 없습니다"));
    }

    public void deleteById(UUID id) {
        gameRepository.deleteById(id);
    }
}

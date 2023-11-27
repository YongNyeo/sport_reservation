package com.example.sports.service;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.GameType;
import com.example.sports.domain.game.dto.RequestGameDto;
import com.example.sports.repository.GameRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private RequestGameDto dto;

    @BeforeEach
    public void init() {

        dto = new RequestGameDto("team1", "team2", "2009-12-05", 10000, 10000, "seoul", GameType.SOCCER);

    }

    @Test
    @DisplayName("save()를 성공적으로 저장한다")
    public void saveSuccess() {
        //given
        UUID id = UUID.randomUUID();
        Game game = Game.builder()
                .id(id)
                .gameType(dto.getGameType())
                .team1(dto.getTeam1())
                .team2(dto.getTeam2())
                .seat(dto.getSeat())
                .price(dto.getPrice()).build();

        given(gameRepository.save(any())).willReturn(game);

        //when
        Game savedGame = gameService.save(dto);

        //then
        verify(gameRepository).save(any());
        Assertions.assertThat(game.getId()).isNotNull();
        Assertions.assertThat(savedGame.getGameType()).isEqualTo(game.getGameType());

    }


    @Test
    @DisplayName("존재하는 id 검색시 성공적으로 game을 반환한다.")
    public void findByIdSuccess() {
        //given
        UUID id = UUID.randomUUID();
        Game game = Game.builder()
                .id(id)
                .gameType(dto.getGameType())
                .team1(dto.getTeam1())
                .team2(dto.getTeam2())
                .seat(dto.getSeat())
                .price(dto.getPrice()).build();

        gameRepository.save(game);
        given(gameRepository.findById(id)).willReturn(Optional.ofNullable(game));

        //when
        Game findGame = gameService.findById(id);

        //then
        Assertions.assertThat(findGame.getId()).isEqualTo(game.getId());

    }

    @Test
    @DisplayName("존재하지 않는 id 검색시 NoSuchElementException 예외가 발생하며 game 반환에 실패한다.")
    public void findByIdFail() {
        //given
        UUID id = UUID.randomUUID();
        given(gameRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when&&then
        Assertions.assertThatThrownBy(() -> gameService.findById(id)).isInstanceOf(NoSuchElementException.class);

    }

    @Test
    @DisplayName("findAll()로 성공적으로 game 리스트를 반환한다.")
    public void findAll() {
        //given
        Game game = Game.builder()
                .id(UUID.randomUUID())
                .gameType(dto.getGameType())
                .team1(dto.getTeam1())
                .team2(dto.getTeam2())
                .seat(dto.getSeat())
                .price(dto.getPrice()).build();

        List<Game>gameList = List.of(game);

        given(gameRepository.findAll()).willReturn(gameList);

        //when
        List<Game> games = gameService.findAll();

        //then
        Assertions.assertThat(games).size().isEqualTo(1);
        Assertions.assertThat(games.get(0).getId()).isEqualTo(game.getId());
    }


    @Test
    @DisplayName("deleteById()를 성공적으로 실행한다.")
    public void deleteByIdSuccess() {
        //given
        UUID id = UUID.randomUUID();

        //when
        gameService.deleteById(id);

        //then
        verify(gameRepository).deleteById(id);
    }
}

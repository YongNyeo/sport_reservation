package com.example.sports.repository;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.GameType;
import com.example.sports.domain.ticket.Ticket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class GameRepositoryTest {
    @Autowired
    private GameRepository gameRepository;

    private Game game;

    @BeforeEach
    public void init() {
         game = Game.builder()
                .gameType(GameType.SOCCER)
                .location("seoul")
                .team1("chelsea")
                .team2("arsnal")
                .price(5000)
                .seat(100000).build();
    }


    @Test
    @DisplayName("save에 성공한다.")
    public void saveSuccess() {
        //give && when
        Game savedGame = gameRepository.save(game);

        //then
        Assertions.assertThat(savedGame.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 id에 대해서는 findById()가 성공한다.")
    public void findByIdSuccess() {

        //given
        gameRepository.save(game);

        //when
        Optional<Game> findGame = gameRepository.findById(game.getId());

        //then
        Assertions.assertThat(findGame).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 id에 대해서는 findById()의 결과가 존재하지 않는다..")
    public void findByIdFail() {

        //given
        gameRepository.save(game);
        UUID notExistId = UUID.randomUUID();

        //when
        Optional<Game> findGame = gameRepository.findById(notExistId);

        //then
        Assertions.assertThat(findGame).isNotPresent();
    }

    @Test
    @DisplayName("findByAll은 성공적으로 실행된다.")
    public void findAllSuccess() {
        //given&&when
        gameRepository.save(game);
        List<Game> games = gameRepository.findAll();

        //then
        Assertions.assertThat(games).isNotEmpty();
    }

    @Test
    @DisplayName("deleteById는 성공적으로 DB 엔티티 삭제가 실행된다.")
    public void deleteByIdSuccess() {
        //given
        Game savedGame = gameRepository.save(game);
        gameRepository.deleteById(savedGame.getId());

        //when
        Optional<Game> findGame = gameRepository.findById(savedGame.getId());

        //then
        Assertions.assertThat(findGame).isNotPresent();
    }
}

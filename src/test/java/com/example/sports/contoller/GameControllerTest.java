package com.example.sports.contoller;

import com.example.sports.controller.GameController;
import com.example.sports.controller.api.GameRestController;
import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.GameType;
import com.example.sports.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({GameRestController.class, GameController.class})
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameService gameService;


    @Test
    @DisplayName("Game 을 생성하는 POST 작업을 수행하고 status 를 201처리한다.")
    public void createPostSuccess() throws Exception {
        //given
        Game game = Game.builder()
                .location("seoul")
                .seat(10000)
                .team1("team1")
                .team2("team2")
                .price(5000)
                .gameType(GameType.SOCCER).build();


        given(gameService.save(any())).willReturn(game);
        String requestJson = "{\"team1\":\"t1\", \"team2\": \"t2\", \"gameDate\":\"2009-12-05\", \"seat\":10000, \"price\":10000, \"location\":\"seoul\", \"gameType\":\"SOCCER\"}";

        //when && then
        mvc.perform(post("/game")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Game 을 삭제하는 DELETE 를 수행하고, status 는 204로 처리한다.")
    public void deleteByIdSuccess() throws Exception {
        //given
        UUID id = UUID.randomUUID();

        //when&&then
        mvc.perform(delete("/game/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

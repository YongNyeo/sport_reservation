package com.example.sports.contoller;

import com.example.sports.controller.ReservationController;
import com.example.sports.controller.api.ReservationRestController;
import com.example.sports.domain.reservation.PaymentStatus;
import com.example.sports.domain.reservation.Reservation;
import com.example.sports.service.ReservationService;
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
@WebMvcTest({ReservationController.class, ReservationRestController.class})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("reservation을 생성하는 POST 수행 후, status 는 201 처리한다.")
    public void createPostSuccess() throws Exception {

        Reservation reservation = Reservation.builder()
                .username("kim")
                .email("won05121@naver.com")
                .status(PaymentStatus.WAITING)
                .build();

        given(reservationService.save(any())).willReturn(reservation);

        String requestJson = "{\"gameId\":\"27068574-389b-44f2-a80f-16cc21302786\", \"username\": \"kim\", \"email\":\"won05121@naver.com\",  \"adultNumber\":5, \"minorNumber\":10 }";
        //when&&then
        mvc.perform(post("/reservation")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("Reservation 을 삭제하는 DELETE 를 수행하고, status 는 204로 처리한다.")
    public void deleteByIdSuccess() throws Exception {
        //given
        UUID id = UUID.randomUUID();

        //when&&then
        mvc.perform(delete("/reservation/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

package com.example.sports.service;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.game.GameType;
import com.example.sports.domain.reservation.PaymentStatus;
import com.example.sports.domain.reservation.Reservation;
import com.example.sports.domain.reservation.dto.RequestReservationDto;
import com.example.sports.domain.ticket.Ticket;
import com.example.sports.domain.ticket.TicketType;
import com.example.sports.exception.custom.MinimumAmountTickets;
import com.example.sports.exception.custom.NotEnoughSeat;
import com.example.sports.repository.GameRepository;
import com.example.sports.repository.ReservationRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private ReservationService reservationService;

    private RequestReservationDto dto;
    private Game game;

    @BeforeEach
    public void init() {

        dto = new RequestReservationDto(UUID.randomUUID(), "kim", "won05121@naver.com", 3, 2);
        game = Game.builder()
                .gameType(GameType.SOCCER)
                .location("seoul")
                .team1("chelsea")
                .team2("arsnal")
                .price(5000)
                .seat(10000).build();
    }

    @Test
    @DisplayName("save()가 성공적으로 작동한다. ")
    public void saveSuccess() {
        //given
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .status(PaymentStatus.WAITING)
                .email(dto.getEmail())
                .username(dto.getUsername())
                .totalSpending(100000L).build();

        when(reservationRepository.save(any())).thenReturn(reservation);
        when(gameRepository.findById(any())).thenReturn(Optional.ofNullable(game));

        //when
        Reservation savedReservation = reservationService.save(dto);

        //then
        verify(reservationRepository).save(any());
        Assertions.assertThat(savedReservation.getEmail()).isEqualTo(dto.getEmail());
        Assertions.assertThat(savedReservation.getId()).isNotNull();
    }

    @Test
    @DisplayName("save() 실행중 dto 티켓의 총 개수가 0개 일때 MinimumAmountTickets 예외가 발생한다.")
    public void saveFailWithMinimumAmountTickets() {
        //given
        RequestReservationDto zeroTicket = new RequestReservationDto(UUID.randomUUID(), "kim", "won05121@naver.com", 0, 0);

        //when&&then
        Assertions.assertThatThrownBy(() -> reservationService.save(zeroTicket)).isInstanceOf(MinimumAmountTickets.class);
    }

    @Test
    @DisplayName("save() 실행중 존재하지 않는 game 을  예약하면 NoSuchElementException 예외가 발생한다.")
    public void saveFailWithNoSuchElementException() {
        //given && when
        when(gameRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        //then
        Assertions.assertThatThrownBy(() -> reservationService.save(dto)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("save() 실행중 티켓 구매수가 잔여 좌석 수보다 많으면 NotEnoughSeat 예외가 발생한다.")
    public void saveFailWithNotEnoughSeat() {

        //given
        RequestReservationDto exceedSeat = new RequestReservationDto(UUID.randomUUID(), "kim", "won05121@naver.com", 9999, 9999);

        //when
        when(gameRepository.findById(any())).thenReturn(Optional.ofNullable(game));

        //then
        Assertions.assertThatThrownBy(() -> reservationService.save(exceedSeat)).isInstanceOf(NotEnoughSeat.class);

    }


    @Test
    @DisplayName("존재하는 id 검색시 성공적으로 reservation을 반환한다.")
    public void findByIdSuccess() {
        //given
        UUID existId = UUID.randomUUID();
        Reservation reservation = Reservation.builder()
                .id(existId)
                .username(dto.getUsername())
                .email(dto.getEmail())
                .status(PaymentStatus.WAITING)
                .build();

        given(reservationRepository.findById(existId)).willReturn(Optional.ofNullable(reservation));

        //when
        Reservation findReservation = reservationService.findById(existId);

        //then
        Assertions.assertThat(findReservation.getId()).isEqualTo(reservation.getId());

    }

    @Test
    @DisplayName("존재하지 않는 id 검색시 NoSuchElementException 예외가 발생하며 reservation 반환에 실패한다.")
    public void findByIdFail() {
        //given
        UUID id = UUID.randomUUID();
        given(reservationRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when&&then
        Assertions.assertThatThrownBy(() -> reservationService.findById(id)).isInstanceOf(NoSuchElementException.class);

    }

    @Test
    @DisplayName("findAll()로 성공적으로 reservation 리스트를 반환한다.")
    public void findAll() {
        //given
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .status(PaymentStatus.WAITING)
                .build();

        List<Reservation> reservationList = List.of(reservation);

        given(reservationRepository.findAll()).willReturn(reservationList);

        //when
        List<Reservation> reservations = reservationService.findAll();

        //then
        Assertions.assertThat(reservations).size().isEqualTo(1);
        Assertions.assertThat(reservations.get(0).getId()).isEqualTo(reservation.getId());
    }


    @Test
    @DisplayName("deleteById()를 성공적으로 실행한다.")
    public void deleteByIdSuccess() {
        //given
        UUID id = UUID.randomUUID();

        Ticket ticket = Ticket.builder()
                .ticketType(TicketType.ADULT)
                .game(game)
                .price(5000).build();
        Reservation reservation = Reservation.builder()
                .id(id)
                .username(dto.getUsername())
                .email(dto.getEmail())
                .status(PaymentStatus.WAITING)
                .tickets(List.of(ticket))
                .build();



        when(reservationRepository.findById(any())).thenReturn(Optional.ofNullable(reservation));

        //when
        reservationService.deleteById(id);

        //then

        verify(reservationRepository).deleteById(id);
    }
}

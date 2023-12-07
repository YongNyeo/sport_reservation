package com.example.sports.service;

import com.example.sports.domain.game.Game;
import com.example.sports.domain.reservation.Reservation;
import com.example.sports.domain.reservation.dto.RequestReservationDto;
import com.example.sports.exception.custom.MinimumAmountTickets;
import com.example.sports.exception.custom.NotEnoughSeat;
import com.example.sports.repository.GameRepository;
import com.example.sports.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GameRepository gameRepository;

    public Reservation save(RequestReservationDto dto) {

        //예매 먼저 생성하고, 티켓은 결제가 완료되면 그때 생성해도 됨.
        //물론 예매에 성공할때 처음에 결제창 유도 할건데 , 안해도 됨(나중에 결제해도됨 .유효시간 30분)
        Reservation reservation = RequestReservationDto.toEntity(dto);

        //티켓 최소 개수에 대한 검증(예외처리)
        if (!reservation.validMinimumTickets()) {
            throw new MinimumAmountTickets();
        }
        //참조하는 경기(game)이 존재하는 지에 대한 검증
        Game game = gameRepository.findById(dto.getGameId()).orElseThrow(NoSuchElementException::new);

        //경기 좌석이 충분한 지에 대한 검증
        if (!game.isEnoughSeat(dto.getAdultNumber() + dto.getMinorNumber())) {
            throw new NotEnoughSeat();
        }

        //예매 성공하면 좌석 수 감소시키기
        game.minusSeat(reservation.getAdultNumber() + reservation.getMinorNumber());

        //payment controller->service에서 실행할것임(결제+티켓 생성)
        return reservationRepository.save(reservation);

    }

    @Transactional(readOnly = true)
    public Reservation findById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예매 내역입니다.")
                );
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    //예매 삭제보다는, 예매 취소 느낌!
    public void deleteById(UUID id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예매 내역입니다."));

        reservationRepository.delete(reservation);
//        reservationRepository.deleteById(id);-->요건 존재하지않는 id에 대한 얘외처리 프리하게 못함!

        gameRepository.findById(reservation.getGame().getId())
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 게임입니다."))
                .plusSeat(reservation.getAdultNumber()+reservation.getMinorNumber());


    }
}

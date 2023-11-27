package com.example.sports.repository;

import com.example.sports.domain.reservation.PaymentStatus;
import com.example.sports.domain.reservation.Reservation;
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
public class ReservationRepositoryTest {

    private Reservation reservation;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void initReservation() {
        reservation = Reservation.builder()
                .email("won05121@naver.com")
                .username("kim")
                .status(PaymentStatus.WAITING).build();
    }

    @Test
    @DisplayName("save()는 성공적으로 실행된다.")
    public void saveSuccess() {
        //given&&when
        Reservation savedReservation = reservationRepository.save(reservation);
        //then
        Assertions.assertThat(savedReservation.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 Id에 대해서 findById()는 성공적으로 실행된다. ")
    public void findByIdSuccess() {
        //given
        Reservation savedReservation = reservationRepository.save(reservation);

        //when
        Optional<Reservation> findReservation = reservationRepository.findById(savedReservation.getId());

        //then
        Assertions.assertThat(findReservation).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 Id에 대해서 findById()의 값은 존재하지 않는다.")
    public void findByIdFail() {
        //given
        Reservation savedReservation = reservationRepository.save(reservation);
        UUID notExistId = UUID.randomUUID();
        //when
        Optional<Reservation> findReservation = reservationRepository.findById(notExistId);

        //then
        Assertions.assertThat(findReservation).isNotPresent();
    }

    @Test
    @DisplayName("findAll()로 성공적으로 reservation을 불러온다.")
    public void findAllSuccess() {

        //given
        reservationRepository.save(reservation);

        //when
        List<Reservation> all = reservationRepository.findAll();

        //then
        Assertions.assertThat(all).size().isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("deleteById()이 성공적으로 실행된다.")
    public void deleteByIdSuccess() {

        //given
        Reservation savedReservation = reservationRepository.save(reservation);
        UUID id = savedReservation.getId();

        //when
        reservationRepository.deleteById(id);

        //then
        Assertions.assertThat(reservationRepository.findById(id)).isNotPresent();
    }
}

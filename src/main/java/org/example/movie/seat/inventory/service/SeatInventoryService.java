package org.example.movie.seat.inventory.service;

import lombok.AllArgsConstructor;
import org.example.movie.core.common.booking.BookingInformation;
import org.example.movie.core.common.schedule.MovieScheduleRequest;
import org.example.movie.core.common.schedule.MovieScheduleResponse;
import org.example.movie.core.common.schedule.MovieShow;
import org.example.movie.seat.inventory.adapter.KafkaMovieScheduleProducerAdapter;
import org.example.movie.seat.inventory.adapter.KafkaProducerAdapter;
import org.example.movie.seat.inventory.entity.MovieSeatMaster;
import org.example.movie.seat.inventory.repository.MovieSeatMasterRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class SeatInventoryService {

    private final MovieSeatMasterRepository movieSeatMasterRepository;
    private final KafkaProducerAdapter kafkaProducerAdapter;
    private final KafkaMovieScheduleProducerAdapter kafkaMovieScheduleProducerAdapter;

    private final EntityManager entityManager;

    public void checkSeatAvailability(String uniqueId, BookingInformation bookingInformation)
            throws ExecutionException, InterruptedException, TimeoutException {
        int seatCount =
                Optional.ofNullable(
                                Optional.ofNullable(kafkaMovieScheduleProducerAdapter.kafkaMovieScheduleRequestReplyObject(
                                                uniqueId,
                                                MovieScheduleRequest
                                                        .of()
                                                        .setMovieScheduleUniqueId(bookingInformation.getScheduleUniqueId())))
                                        .orElseGet(MovieScheduleResponse::of)
                                        .getMovieShow())
                        .orElseGet(MovieShow::of).getSeatCount();
        if (seatCount > 0) {
            MovieSeatMaster movieSeatMaster =
                    movieSeatMasterRepository.findByMovieScheduleUniqueId(
                                    bookingInformation.getScheduleUniqueId())
                            .orElseGet(MovieSeatMaster::new);
            if (movieSeatMaster.getMsId()<= 0) {
                movieSeatMaster.setSeatRemaining(seatCount
                        - bookingInformation.getNumberOfSeat());
                movieSeatMaster.setMovieScheduleUniqueId(bookingInformation.getScheduleUniqueId());
            } else {
                movieSeatMaster.setSeatRemaining(movieSeatMaster.getSeatRemaining()
                        - bookingInformation.getNumberOfSeat());
            }
            movieSeatMasterRepository.save(movieSeatMaster);
            kafkaProducerAdapter.sendBookingInformationMessage(uniqueId, bookingInformation);
        }
    }
}

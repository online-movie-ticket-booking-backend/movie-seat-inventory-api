package org.example.movie.seat.inventory.adapter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.movie.core.common.booking.BookingInformation;
import org.example.movie.seat.inventory.service.SeatInventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaConsumerAdapter {

    private final SeatInventoryService seatInventoryService;

    @KafkaListener(topics = "${kafka.movieBookingApi.checkSeatInventory.topic.request}", groupId = "movie-booking")
    public void listenCheckSeatInventory(
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String uniqueId,
            BookingInformation message)
            throws ExecutionException, InterruptedException, TimeoutException {
        seatInventoryService.checkSeatAvailability(uniqueId,message);
    }
}

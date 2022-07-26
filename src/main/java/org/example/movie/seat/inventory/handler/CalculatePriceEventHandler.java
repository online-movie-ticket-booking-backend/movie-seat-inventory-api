package org.example.movie.seat.inventory.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.example.movie.core.common.booking.BookingInformation;
import org.example.movie.seat.inventory.event.CalculatePriceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Slf4j
@Component
public class CalculatePriceEventHandler {

    @Value("${kafka.movieBookingApi.calculatePrice.topic.request}")
    private String calculatePriceTopicName;

    private KafkaTemplate<String, BookingInformation> kafkaTemplateBookingInformation;

    @Autowired
    public void setKafkaTemplateBookingInformation(
            KafkaTemplate<String, BookingInformation> kafkaTemplateBookingInformation){
        this.kafkaTemplateBookingInformation=kafkaTemplateBookingInformation;
    }

    @EventHandler
    public void on(CalculatePriceEvent calculatePriceEvent)
            throws ExecutionException, InterruptedException, TimeoutException {
        kafkaTemplateBookingInformation.send(calculatePriceTopicName,
                calculatePriceEvent.getUniqueId(),
                calculatePriceEvent.getBookingInformation());
    }
}

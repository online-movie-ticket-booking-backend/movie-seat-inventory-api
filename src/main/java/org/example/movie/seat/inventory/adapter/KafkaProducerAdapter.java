package org.example.movie.seat.inventory.adapter;

import lombok.AllArgsConstructor;
import org.example.movie.core.common.booking.BookingInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerAdapter {

    @Value("${kafka.movieBookingApi.calculatePrice.topic.request}")
    private String calculatePriceTopic;

    private KafkaTemplate<String, BookingInformation> kafkaTemplate;

    @Autowired
    private void setKafkaTemplate(KafkaTemplate<String, BookingInformation> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void sendBookingInformationMessage(String uniqueId, BookingInformation message) {
        kafkaTemplate.send(calculatePriceTopic,uniqueId, message);
    }

}

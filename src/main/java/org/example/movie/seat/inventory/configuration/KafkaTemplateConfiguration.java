package org.example.movie.seat.inventory.configuration;

import org.example.movie.core.common.booking.BookingInformation;
import org.example.movie.core.common.schedule.MovieScheduleRequest;
import org.example.movie.core.common.schedule.MovieScheduleResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaTemplateConfiguration {
    @Bean
    public KafkaTemplate<String, BookingInformation> kafkaTemplateBookingInformation(ProducerFactory<String,
            BookingInformation> producerFactoryBookingInformation) {
        return new KafkaTemplate<>(producerFactoryBookingInformation);
    }

    @Bean
    public ReplyingKafkaTemplate<String, MovieScheduleRequest, MovieScheduleResponse> kafkaMovieScheduleReplyTemplate(
            ProducerFactory<String, MovieScheduleRequest> producerFactoryMovieScheduleRequest,
            ConcurrentMessageListenerContainer<String, MovieScheduleResponse> movieScheduleResponseListenerContainer) {
        return new ReplyingKafkaTemplate<>(producerFactoryMovieScheduleRequest, movieScheduleResponseListenerContainer);
    }

}

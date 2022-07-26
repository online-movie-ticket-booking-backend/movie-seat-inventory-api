package org.example.movie.seat.inventory.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.movie.core.common.booking.BookingInformation;
import org.example.movie.core.common.schedule.MovieScheduleResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.movieBookingApi.groupName}")
    private String groupName;

    @Value("${kafka.movieBookingApi.movieSchedule.topic.response-unique-id}")
    private String movieScheduleResponseTopic;

    @Value("${kafka.movieBookingApi.movieSchedule.topic.serialization-class}")
    private String movieScheduleSerializationClass;

    @Value("${kafka.movieBookingApi.checkSeatInventory.topic.serialization-class}")
    private String checkSeatInventorySerializationClass;

    @Bean
    public ConsumerFactory<String, BookingInformation> consumerFactoryBookingInformation() {
        return new DefaultKafkaConsumerFactory<>(getConfigurationMapForListener(checkSeatInventorySerializationClass));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingInformation> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookingInformation> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryBookingInformation());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovieScheduleResponse> consumerFactoryMovieScheduleResponse() {
        return new DefaultKafkaConsumerFactory<>(
                getConfigurationMapForListener(movieScheduleSerializationClass));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovieScheduleResponse> movieScheduleResponseListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovieScheduleResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryMovieScheduleResponse());
        return factory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, MovieScheduleResponse> movieScheduleResponseListenerContainer(
            ConcurrentKafkaListenerContainerFactory<String, MovieScheduleResponse> movieScheduleResponseListenerContainerFactory) {
        ConcurrentMessageListenerContainer<String, MovieScheduleResponse> repliesContainer =
                movieScheduleResponseListenerContainerFactory.createContainer(movieScheduleResponseTopic);
        repliesContainer.getContainerProperties().setGroupId(groupName);
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    private Map<String, Object> getConfigurationMapForListener(String defaultValueType) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(JsonDeserializer.KEY_DEFAULT_TYPE, "java.lang.String");
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, defaultValueType);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "org.example.movie.core.common.schedule");
        return configProps;
    }
}

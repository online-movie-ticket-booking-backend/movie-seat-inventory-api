package org.example.movie.seat.inventory.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.movie.core.common.schedule.MovieScheduleRequest;
import org.example.movie.core.common.schedule.MovieScheduleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaMovieScheduleProducerAdapter {

    @Value("${kafka.movieBookingApi.movieSchedule.topic.request-unique-id}")
    private String movieScheduleTopicName;
    private ReplyingKafkaTemplate<String, MovieScheduleRequest, MovieScheduleResponse> kafkaMovieScheduleReplyTemplate;

    @Autowired(required = true)
    public void setKafkaMovieScheduleReplyTemplate(ReplyingKafkaTemplate<String, MovieScheduleRequest,
            MovieScheduleResponse> kafkaMovieScheduleReplyTemplate) {
        this.kafkaMovieScheduleReplyTemplate = kafkaMovieScheduleReplyTemplate;
    }


    public MovieScheduleResponse kafkaMovieScheduleRequestReplyObject(
            String uniqueId, MovieScheduleRequest movieScheduleRequest) throws
            ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, MovieScheduleRequest> record =
                new ProducerRecord<>(movieScheduleTopicName, uniqueId, movieScheduleRequest);
        RequestReplyFuture<String, MovieScheduleRequest, MovieScheduleResponse> replyFuture =
                kafkaMovieScheduleReplyTemplate.sendAndReceive(record);
        SendResult<String, MovieScheduleRequest> sendResult =
                replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        ConsumerRecord<String, MovieScheduleResponse> consumerRecord =
                replyFuture.get(10, TimeUnit.SECONDS);
        return consumerRecord.value();
    }
}

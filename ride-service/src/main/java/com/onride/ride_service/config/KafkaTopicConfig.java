package com.onride.ride_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.ride-requested-topic}")
    private String rideRequestedTopic;

    @Bean
    public NewTopic rideRequestedTopic() {
        return TopicBuilder.name(rideRequestedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
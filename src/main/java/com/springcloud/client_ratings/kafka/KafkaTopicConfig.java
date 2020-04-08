package com.springcloud.client_ratings.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "localhost:9092")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic postTopic() {
        return new NewTopic("post-ratings", 1, (short) 1);
    }

    @Bean
    public NewTopic putTopic() {
        return new NewTopic("put-ratings", 1, (short) 1);
    }

    @Bean
    public NewTopic patchTopic() {
        return new NewTopic("patch-ratings", 1, (short) 1);
    }

    @Bean
    public NewTopic deleteTopic() {
        return new NewTopic("delete-ratings", 1, (short) 1);
    }
}
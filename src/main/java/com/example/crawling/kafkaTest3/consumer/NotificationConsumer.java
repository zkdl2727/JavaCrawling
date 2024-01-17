package com.example.crawling.kafkaTest3.consumer;

import com.example.crawling.kafkaTest3.dto.Payload;
import com.example.crawling.kafkaTest3.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class NotificationConsumer {
    private static final String TOPIC_NAME = "kafkaTest3";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "NotificationGroup");


        Consumer<String, String>  consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));


        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

            for (ConsumerRecord<String, String> record : records) {
                String json = record.value();
                try {
                    Payload payload = Payload.fromJson(json);
                    NotificationService notificationService = new NotificationService();
                    notificationService.sendPushNotification(payload.getUserEmail(), payload.getMessage());
                    // System.out.println("새 알림 :" + payload.getMessage()); 콘솔 응답 처리
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

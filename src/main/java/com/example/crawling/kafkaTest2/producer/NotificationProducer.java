package com.example.crawling.kafkaTest2.producer;
import com.example.crawling.kafkaTest2.dto.Notification;
import com.example.crawling.kafkaTest2.service.NotificationService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
public class NotificationProducer {

    private static final String TOPIC_NAME = "kafkaTest2";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        Producer<String,String> producer = new KafkaProducer<>(properties);

        NotificationService notificationService = new NotificationService();
        Notification notification = notificationService.createNotification("zkdl6565@naver.com","zkdl6565@naver.com send you a message");

        String message = String.format("TO: %s, Message : %s", notification.getEmail(), notification.getMessage());

        producer.send(new ProducerRecord<>(TOPIC_NAME, message), (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Sent message: (" + metadata.topic() + ", " + metadata.partition() + ", " + metadata.offset() + ")");
            }
        });
        producer.close();


    }
}

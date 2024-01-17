package com.example.crawling.kafkaTest3.producer;

import com.example.crawling.kafkaTest3.dto.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;


public class NotificationProducer {

    private static final String TOPIC_NAME = "kafkaTest3";

    public static void main(String[] args) throws JsonProcessingException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String,String> producer = new KafkaProducer<>(properties);

        Payload payload = new Payload("zkdl6565@naver.com","새로운 메시지 발송");

        producer.send(new ProducerRecord<>(TOPIC_NAME, payload.toJson()));

        producer.close();
    }
}

package com.example.crawling;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaProducerConsumerTest {

    private static final String TOPIC_NAME = "kafkaTest1";

    @Test
    public void testProducerAndConsumer() {

        // producer 설정.
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "localhost:9092");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(producerProperties);

        String key = "사용자1";
        String value = "this is a push notification";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, key, value);

        producer.send(record);
        producer.close();

        // consumer 설정.

        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "localhost:9092");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("group.id", "NotificationGroup");

        Consumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerProperties);

        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> receivedRecord : records) {
            assertEquals(key, receivedRecord.key());
            assertEquals(value, receivedRecord.value());
        }
        consumer.close();
    }
}

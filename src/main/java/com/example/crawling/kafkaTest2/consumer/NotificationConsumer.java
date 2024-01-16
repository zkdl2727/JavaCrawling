package com.example.crawling.kafkaTest2.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class NotificationConsumer {
    private static final String TOPIC_NAME = "kafkaTest2";
    private static final String FIN_MESSAGE = "exit";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "NotificationGroup");

        Consumer<String,String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME)); // 왜 singletonlist로 갖고 오지?

        String message = null;

        try {
            do{
                ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100000));
                for(ConsumerRecord<String,String> record:records) {
                    message = record.value();
                    System.out.println("알림 : " + message);
                }
            }while (message!=null && !FIN_MESSAGE.equals(message));
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("알림 처리간 에러 발생");
        }finally {
            consumer.close();
        }
    }
}

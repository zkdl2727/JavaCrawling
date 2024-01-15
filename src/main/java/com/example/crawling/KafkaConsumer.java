package com.example.crawling;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumer {
    private static final String TOPIC_NAME = "kafkaTest1";
    private static final String FIN_MESSAGE = "exit";


    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092"); // 카프카 서버의 위치를 지정.
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); // 메시지의 키를 역직렬화하는데 사용한 deserializer(byte -> String)클래스 지정.
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); // 메시지의 값을 역직렬화하는데 사용한 deserializer(byte -> String)클래스 지정.
        properties.put("group.id", "NotificationGroup");

        Consumer<String,String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));


        String message = null;

        try {
            do{
                ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(10000));
                for(ConsumerRecord<String,String> record : records) {
                    message = record.value();
                    System.out.println(message);
                }
            }while (!StringUtils.pathEquals(message,FIN_MESSAGE));
        }catch (Exception e) {
            System.out.println("예외2");
        }finally {
            consumer.close();
        }
    }
}

/*      이전 kafka Topic 이름 넣는 코드.
        String topic = "kafkaTest1";
        consumer.subscribe(Arrays.asList(topic));*/

/*          이전 ConsumerRecord 기록 가져오는 코드.
            while (true) {
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record:records) {
                System.out.printf("Received notification: %s%n", record.value());
            }
        }
*/

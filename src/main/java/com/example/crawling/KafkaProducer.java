package com.example.crawling;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class KafkaProducer {
    private static final String TOPIC_NAME = "kafkaTest1";
    private static final String FIN_MESSAGE = "exit";


    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092"); // 카프카 서버의 위치를 지정.
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // 메시지의 키를 직렬화하는데 사용한 serializer(String -> byte)클래스 지정.
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // 메시지의 값을 직렬화하는데 사용한 serializer(String -> byte)클래스 지정.

        Producer<String,String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);


        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("입력 >");
            String message = sc.nextLine();

            ProducerRecord<String,String> record = new ProducerRecord<>(TOPIC_NAME,message);
            try {
                producer.send(record,(metadata, exception) -> {
                    if(exception !=null){
                        System.out.println("예외");
                    }
                });
            }catch (Exception e) {
                System.out.println("예외1");
            }finally {
                producer.flush();
            }

            if(StringUtils.pathEquals(message,FIN_MESSAGE)) {
                producer.close();
                break;
            }
        }
    }
}


/*      // 이전 코드
        String topic = "kafkaTest1";
        String key = "사용자1";
        String value = "this is a push notification";

        ProducerRecord<String, String> record = new ProducerRecord<>(topic,key,value);

        producer.send(record);

        producer.close();*/
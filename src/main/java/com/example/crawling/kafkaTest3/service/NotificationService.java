package com.example.crawling.kafkaTest3.service;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
public class NotificationService {
    public void sendPushNotification(String deviceToken, String message) {
        Message message1 = Message.builder()
                .putData("message", message)
                .setToken(deviceToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message1);
            System.out.println("메시지 전송 성공 : " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("메시지 전송 실패 : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

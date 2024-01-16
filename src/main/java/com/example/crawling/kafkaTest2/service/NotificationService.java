package com.example.crawling.kafkaTest2.service;

import com.example.crawling.kafkaTest2.dto.Notification;

public class NotificationService {

    public Notification createNotification(String email, String message) {
        return new Notification(email,message);
    }
}

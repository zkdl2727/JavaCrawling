package com.example.crawling.kafkaTest3.dto;

import org.springframework.boot.json.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Payload {

    private String userEmail;
    private String message;

    public Payload(String userEmail, String message) {
        this.userEmail = userEmail;
        this.message = message;
    }

    public static Payload fromJson(String json) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Payload.class);
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

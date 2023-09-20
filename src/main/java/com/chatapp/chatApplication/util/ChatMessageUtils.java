package com.chatapp.chatApplication.util;

import com.chatapp.chatApplication.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class ChatMessageUtils {
    private ObjectMapper objectMapper = new ObjectMapper();

    public String convertMessageToJson(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            // Handle serialization error
            e.printStackTrace();
            return null;
        }
    }

    public Message convertJsonToMessage(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (Exception e) {
            // Handle deserialization error
            e.printStackTrace();
            return null;
        }
    }

    public String generateOneToOneRoom(String senderId, String recipientId) {
        String[] sortedUserIds = {senderId, recipientId};
        Arrays.sort(sortedUserIds);
        String roomName = sortedUserIds[0] + "-" + sortedUserIds[1];
        return roomName;
    }
}
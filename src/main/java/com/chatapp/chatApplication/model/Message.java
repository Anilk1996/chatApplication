package com.chatapp.chatApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private MessageType type;
    private String message;
    private String room;

    private String senderUserId;
    private String recipientUserId;
}


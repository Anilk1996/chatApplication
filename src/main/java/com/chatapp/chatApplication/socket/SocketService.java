package com.chatapp.chatApplication.socket;

import com.chatapp.chatApplication.model.Message;
import com.chatapp.chatApplication.model.MessageType;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocketService {

    public void sendMessage(Message messageData, String eventName, SocketIOClient senderClient, String roomName) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomName).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName, new Message(MessageType.SERVER, messageData.getMessage(), messageData.getRoom(), messageData.getSenderUserId(), messageData.getRecipientUserId()));
            }
        }
    }

}

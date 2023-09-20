package com.chatapp.chatApplication.socket;

import com.chatapp.chatApplication.model.Message;
import com.chatapp.chatApplication.util.ChatMessageUtils;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;
    private final SocketService socketService;
    private final Jedis jedis;
    ConcurrentHashMap<String, SocketIOClient> connectedClients = new ConcurrentHashMap<>();

    public SocketModule(SocketIOServer server, SocketService socketService, Jedis jedis) {
        this.server = server;
        this.socketService = socketService;
        this.jedis = jedis;

        server.addDisconnectListener(client -> {
            String sessionId = client.getSessionId().toString();
            connectedClients.remove(sessionId);
            System.out.println("Client disconnected: " + sessionId);
        });
        server.addEventListener("initiateChat", Message.class, (socketIOClient, data, ackRequest) -> {
            String roomName = ChatMessageUtils.generateOneToOneRoom(data.getSenderUserId(), data.getRecipientUserId());
            System.err.println(roomName);
            // Join the room
            socketIOClient.joinRoom(roomName);
            log.info("Socket ID[{}]  Connected to socket", socketIOClient.getSessionId().toString());
        });
        server.addEventListener("private-message", Message.class, (socketIOClient, data, ackRequest) -> {
            String roomName = ChatMessageUtils.generateOneToOneRoom(data.getSenderUserId(), data.getRecipientUserId());
            String messageJson = ChatMessageUtils.convertMessageToJson(data); // Convert Message to JSON
            jedis.rpush(roomName, messageJson);
            socketService.sendMessage(data, "get_private-message", socketIOClient, roomName);

        });
        server.addEventListener("chat-history", Message.class, ((socketIOClient, data, ackRequest) -> {
            String roomName = ChatMessageUtils.generateOneToOneRoom(data.getSenderUserId(), data.getRecipientUserId());
            List<String> messageJsonList = jedis.lrange(roomName, 0, -1);
            List<Message> messages = new ArrayList<>();

            for (String messageJson : messageJsonList) {
                Message message = ChatMessageUtils.convertJsonToMessage(messageJson); // Convert JSON to Message
                messages.add(message);
            }
            socketIOClient.sendEvent("get-chat-history", messages);
        }));

        server.addEventListener("sendMessage", Message.class, onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessage(data, "get_message", senderClient, "");
        };
    }
}

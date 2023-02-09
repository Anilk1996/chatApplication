package com.chatapp.chatApplication.socket;

import com.chatapp.chatApplication.model.Message;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService){
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("sendMessage", Message.class, onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
//            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message",data.getMessage());
            socketService.sendMessage(data.getRoom(), "get_message", senderClient, data.getMessage());

        };
    }

    private ConnectListener onConnected(){
        return client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Socket ID[{}] Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected(){
        return client -> {
            log.info("Socket ID[{}] Disconnected to socket", client.getSessionId().toString());
        };
    }
}

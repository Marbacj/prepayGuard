package com.mapoh.ppg.server;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mabohv
 * @date 2025/2/17 10:01
 */

@ServerEndpoint("/ws/{userId}")
public class WebSocketServer {

    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        userSessions.put(userId, session);
        System.out.println("User " + userId + " connected.");
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        userSessions.remove(userId);
        System.out.println("User " + userId + " disconnected.");
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        System.out.println("Received message from user " + userId + ": " + message);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public static void sendMessage(String userId, String message) {
        Session session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

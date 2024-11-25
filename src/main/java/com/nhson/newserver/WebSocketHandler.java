package com.nhson.newserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);
    private static boolean onLed = false;

    private static float distanceThreshold = 9999;
    private static CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        LOG.info("New WebSocket connection established. Total clients: {}", sessions.size());
        session.sendMessage(new TextMessage("{led:" + String.valueOf(onLed) + " , distance:" + String.valueOf(distanceThreshold) + "}"));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        LOG.info("Received message from client: {}", payload);

        distanceThreshold = Float.parseFloat(payload);

        broadcastLedStatus();
    }

    private void broadcastLedStatus() throws IOException {
        TextMessage message = new TextMessage("{led:" + String.valueOf(onLed) + " , distance:" + String.valueOf(distanceThreshold) + "}");

        LOG.info("Broadcasting LED status to {} clients: {}", sessions.size(), message.getPayload());

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                    LOG.info("Sent LED status to client: {}", session.getId());
                } catch (IOException e) {
                    LOG.error("Failed to send message to client: {}", session.getId(), e);
                }
            } else {
                LOG.warn("Skipping closed session: {}", session.getId());
                sessions.remove(session);
            }
        }
    }

    public float getDistanceThreshold() {
        return distanceThreshold;
    }

    public boolean getLedStatus() {
        return onLed;
    }

    public void setLedStatus(boolean onLed) throws IOException {
        WebSocketHandler.onLed = onLed;
        broadcastLedStatus();
    }

    public void setDistanceThreshold(float distanceThreshold) throws IOException {
        WebSocketHandler.distanceThreshold = distanceThreshold;
        broadcastLedStatus();
    }

}

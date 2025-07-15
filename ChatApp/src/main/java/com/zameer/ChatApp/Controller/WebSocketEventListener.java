package com.zameer.ChatApp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private PresenceTracker presenceTracker;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        String username = event.getUser().getName();
        presenceTracker.userConnected(username);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        presenceTracker.userDisconnected(username);
        // update lastSeen in DB
    }
}

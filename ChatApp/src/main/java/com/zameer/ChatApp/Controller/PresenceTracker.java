package com.zameer.ChatApp.Controller;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PresenceTracker {
    private final Set<String> onlineUsers = ConcurrentHashMap
            .newKeySet();

    public void userConnected(String username) {
        onlineUsers.add(username);
    }

    public void userDisconnected(String username) {
        onlineUsers.remove(username);
    }

    public boolean isOnline(String username) {
        return onlineUsers.contains(username);
    }
}

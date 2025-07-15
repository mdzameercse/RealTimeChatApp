package com.zameer.ChatApp.DTO;

import java.time.LocalDateTime;

public class ChatListView {
    private int userId;
    private String username;
    private LocalDateTime timestamp;

    public ChatListView(int userId, String username, LocalDateTime timestamp) {
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Getters and Setters
}

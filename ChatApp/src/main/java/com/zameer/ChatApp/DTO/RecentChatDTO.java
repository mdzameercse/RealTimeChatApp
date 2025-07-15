package com.zameer.ChatApp.DTO;

import java.time.LocalDateTime;

public class RecentChatDTO {
    private int userId;
    private String username;
    private LocalDateTime lastMessageTime;

    public RecentChatDTO(int userId, String username, LocalDateTime lastMessageTime) {
        this.userId = userId;
        this.username = username;
        this.lastMessageTime = lastMessageTime;
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

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
    // Getters and setters
}

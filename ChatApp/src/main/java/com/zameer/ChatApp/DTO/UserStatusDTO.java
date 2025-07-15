package com.zameer.ChatApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserStatusDTO {
    private boolean online;
    private LocalDateTime lastSeen;
}


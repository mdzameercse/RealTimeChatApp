package com.zameer.ChatApp.DTO;

import java.time.LocalDateTime;

public interface ChatMessageView {
    String getContent();
    LocalDateTime getTimestamp();
    String getSenderUsername();

}

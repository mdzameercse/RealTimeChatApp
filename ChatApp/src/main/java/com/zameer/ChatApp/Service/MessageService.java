package com.zameer.ChatApp.Service;

import com.zameer.ChatApp.DTO.ChatMessageView;
import com.zameer.ChatApp.Model.Message;
import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    public Message sendMessage(User currentUser, User frd, String content) {
        Message message = new Message();
        message.setSender(currentUser);
        message.setReceiver(frd);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now()); // ✅ Make sure timestamp is set
        return messageRepo.save(message);
    }

    public List<ChatMessageView>findChatBetweenUsers(Integer currentUserId, Integer friendId) {
        return messageRepo.findChatBetweenUsers(currentUserId,friendId);
    }
}

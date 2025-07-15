package com.zameer.ChatApp.Controller;

import com.zameer.ChatApp.DTO.ChatMessageDTO;
import com.zameer.ChatApp.DTO.ChatMessageView;
import com.zameer.ChatApp.Model.Message;
import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Repository.MessageRepo;
import com.zameer.ChatApp.Service.MessageService;
import com.zameer.ChatApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("message")
public class MessagerController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    MessageRepo messageRepo;
    @PostMapping("sendto/{fid}")
    public String sendMessage(@PathVariable Integer fid, Principal principal, @RequestBody String content) {
        User currentUser = userService.findUserByUsername(principal.getName());
        Optional<User> frd = userService.findUserByUserId(fid);

        Message message = new Message();
        message.setSender(currentUser);
        message.setReceiver(frd.orElse(null));
        message.setContent(content);
        messageRepo.save(message);

        // ✅ Broadcast to sender and receiver
        ChatMessageDTO messageDTO = new ChatMessageDTO();
        messageDTO.setContent(content);
        messageDTO.setTimestamp(LocalDateTime.now());
        messageDTO.setSenderUsername(currentUser.getUsername());
        messageDTO.setReceiverUsername(frd.get().getUsername());

        // Send to both users
        messagingTemplate.convertAndSend("/topic/chat/" + currentUser.getUsername(), messageDTO);
        messagingTemplate.convertAndSend("/topic/chat/" + frd.get().getUsername(), messageDTO);

        return "success";
    }

    @GetMapping("/showchat/{friendId}")
    public List<ChatMessageView> getChat(@PathVariable Integer friendId, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return messageService.findChatBetweenUsers(currentUser.getId(), friendId);
    }


}

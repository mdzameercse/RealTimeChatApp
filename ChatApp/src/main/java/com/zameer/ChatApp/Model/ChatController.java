package com.zameer.ChatApp.Model;

import com.zameer.ChatApp.DTO.ChatListView;
import com.zameer.ChatApp.DTO.RecentChatDTO;
import com.zameer.ChatApp.Repository.MessageRepo;
import com.zameer.ChatApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private UserService userService;
    @Autowired
    MessageRepo messageRepo;
    @GetMapping("/chat/list")
    public List<RecentChatDTO> getRecentChats(Principal principal) {
        String currentUser = principal.getName();
        return messageRepo.findRecentChats(currentUser);
    }



}

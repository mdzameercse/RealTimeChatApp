package com.zameer.ChatApp.Controller;

import com.zameer.ChatApp.DTO.UserStatusDTO;
import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class Usercontroller {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PresenceTracker presenceTracker;
    @GetMapping("showallusers")
    public List<User> showAllUsers(Principal principal) {
        String currentUsername = principal.getName();
        return userService.getUsersNotFriends(currentUsername);
    }

    //    @PostMapping("login")
//    public String Login(@RequestBody User user) {
//        User existingUser = userService.findByUsername(user.getUsername());
//        if (existingUser == null) {
//            return "User not found";
//        }
//        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
//            return "Incorrect password";
//        }
//        return "Login successful";
//    }
    @PostMapping("register")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        userService.addNewUser(user);
        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("me")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(user);
    }
    @GetMapping("status/{username}")
    public Map<String, Object> getStatus(@PathVariable String username) {
        boolean online = presenceTracker.isOnline(username);
        LocalDateTime lastSeen = userService.getLastSeen(username);

        return Map.of(
                "online", online,
                "lastSeen", lastSeen
        );
    }
}
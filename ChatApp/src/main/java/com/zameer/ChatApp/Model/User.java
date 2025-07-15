package com.zameer.ChatApp.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(unique=true,nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(name = "is_online")
    private boolean isOnline=false;
    @Column(name = "last_seen")
    private LocalDateTime lastSeen=LocalDateTime.now();
    private LocalDateTime createTime=LocalDateTime.now();
}

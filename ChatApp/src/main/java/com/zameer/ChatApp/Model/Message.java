package com.zameer.ChatApp.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id",nullable = false)
    private User receiver;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;
    private LocalDateTime timestamp=LocalDateTime.now();
}

package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String message;
    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String avatar;

    public Long getId() {return id;};
    public void setId(Long id) {this.id = id;};
    public String getUsername() {return username;};
    public void setUsername(String name) {this.username = name;};
    public String getMessage() {return message;};
    public void setMessage(String message) {this.message = message;};
    public LocalDateTime getTimestamp() {return timestamp;};
    public void setTimestamp(LocalDateTime timestamp) {this.timestamp = timestamp;};
    public String getAvatar() {return avatar;};
    public void setAvatar(String avatar) {this.avatar = avatar;};
}

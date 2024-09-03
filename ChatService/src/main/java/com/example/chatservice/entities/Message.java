package com.example.chatservice.entities;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;
    private boolean read;
}

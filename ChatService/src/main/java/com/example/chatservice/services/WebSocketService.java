package com.example.chatservice.services;

import com.example.chatservice.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Controller
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/broadcast")
    @SendTo("/topic/admin")
    public Message receiveMessage(@Payload Message message){
        System.out.println("Broadcast message received" + message.toString());
        simpMessagingTemplate.convertAndSend("/topic/admin",message);
        return message;
    }

    @MessageMapping("/private")
    public Message recMessage(@Payload Message message){
        System.out.println("Private message received" + message.toString());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(),"/private",message);
        return message;
    }

}

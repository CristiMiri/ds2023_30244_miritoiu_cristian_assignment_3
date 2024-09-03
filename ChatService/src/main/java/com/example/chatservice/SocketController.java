package com.example.chatservice;

import com.example.chatservice.entities.Message;
import com.example.chatservice.services.MessageService;
import com.example.chatservice.services.WebSocketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/socket")
public class SocketController {
//
//    private final MessageService messageService;
//    private final WebSocketService webSocketService;
//
//    public SocketController(MessageService messageService, WebSocketService webSocketService) {
//        this.messageService = messageService;
//        this.webSocketService = webSocketService;
//    }

//    @PostMapping("/sendprivate")
//    public void sendMessage(@RequestBody Message message) {
//        messageService.saveMessage(message);
//        System.out.println(message.toString());
//        webSocketService.sendMessageToUser(message.getReceiver(), message.toString());
//    }
//
//    @PostMapping("/sendbroadcast")
//    public void sendBroadCast(@RequestBody Message message) {
//        messageService.saveMessage(message);
//        System.out.println(message.toString());
//        webSocketService.sendMessageToTopic(message.toString());
//    }

}

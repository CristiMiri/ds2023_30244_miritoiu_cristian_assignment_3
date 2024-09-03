package com.example.chatservice.services;

import com.example.chatservice.entities.Message;
import com.example.chatservice.repositories.MessageStorage;
import org.springframework.stereotype.Service;

@Service

public class MessageService {

    private MessageStorage messageStorage;

    public MessageService() {
        this.messageStorage = new MessageStorage();
    }


    public void markMessageAsRead(Long messageId) {
        Message message = messageStorage.getMessage(messageId);
        if (message != null) {
            message.setRead(true);
            messageStorage.updateMessage(messageId, message);
            // Optionally, notify users via WebSocket here
        }
    }
    public void saveMessage(Message message) {
        messageStorage.saveMessage(message);
        // Optionally, notify users via WebSocket here
    }

    // Other service methods
}

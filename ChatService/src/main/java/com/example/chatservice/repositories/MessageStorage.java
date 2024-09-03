package com.example.chatservice.repositories;

import com.example.chatservice.entities.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageStorage {
    private Map<Long, Message> messages = new ConcurrentHashMap<>();

    public void saveMessage(Message message) {
        messages.put(message.getId(), message);
    }

    public Message getMessage(Long id) {
        return messages.get(id);
    }

    public void updateMessage(Long id, Message updatedMessage) {
        messages.put(id, updatedMessage);
    }

    public void deleteMessage(Long id) {
        messages.remove(id);
    }

    public Map<Long, Message> getAllMessages() {
        return messages;
    }


}

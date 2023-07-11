package com.oxog.userservice.controller;

import com.oxog.userservice.model.requestModel.chat.ChatUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/")
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    public ChatController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatUser message, SimpMessageHeaderAccessor headerAccessor) {
        // Handle the received message
        log.info("Received message: {}", message.getContent());

        // Broadcast the message to all connected clients
        messagingTemplate.convertAndSend("/topic/chat", message);
    }


}

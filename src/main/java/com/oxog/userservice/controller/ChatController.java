package com.oxog.userservice.controller;

import com.oxog.userservice.model.requestModel.chat.ChatUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/")
public class ChatController {

    @MessageMapping("/setUser")
    public void setUser(ChatUser chatUser, SimpMessageHeaderAccessor headerAccessor) {
        log.info(headerAccessor.getUser().getName());
    }

}

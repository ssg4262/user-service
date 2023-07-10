package com.oxog.userservice.service.chatService;

import com.oxog.userservice.model.ChatMessageDto;

import java.util.List;

public interface ChatMessageService {
    ChatMessageDto saveChatMessage(ChatMessageDto chatMessageDto);
    List<ChatMessageDto> getChatMessagesByUserId(String userId);
}

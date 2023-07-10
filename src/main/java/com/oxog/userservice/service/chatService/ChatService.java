package com.oxog.userservice.service.chatService;

import com.oxog.userservice.model.responseModel.chatDto.ChatMessageDto;
import com.oxog.userservice.model.requestModel.chat.CreateChatMessageRequest;

import java.util.List;

public interface ChatService {
    ChatMessageDto sendChatMessage(CreateChatMessageRequest createChatMessageRequest);

    List<ChatMessageDto> getUserChatMessages(String userId);
}

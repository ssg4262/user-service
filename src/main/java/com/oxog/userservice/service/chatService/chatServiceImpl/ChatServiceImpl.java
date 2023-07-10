package com.oxog.userservice.service.chatService.chatServiceImpl;

import com.oxog.userservice.Entity.UserEntity;
import com.oxog.userservice.Entity.chat.ChatMessageEntity;
import com.oxog.userservice.model.ChatMessageDto;
import com.oxog.userservice.model.requestModel.CreateChatMessageRequest;
import com.oxog.userservice.repository.ChatMessageRepository;
import com.oxog.userservice.repository.UserRepository;
import com.oxog.userservice.service.chatService.ChatService;
import jakarta.ws.rs.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    ModelMapper modelMapper = new ModelMapper();

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ChatMessageDto sendChatMessage(CreateChatMessageRequest createChatMessageRequest) {
        UserEntity userEntity = userRepository.findByUserId(createChatMessageRequest.getUserId());
        if(userEntity == null) throw new NotFoundException("Not Found User");

        ChatMessageEntity newMessageEntity = modelMapper.map(createChatMessageRequest, ChatMessageEntity.class);
        newMessageEntity.setSender(userEntity.getName());

        ChatMessageEntity savedMessageEntity = chatMessageRepository.save(newMessageEntity);
        return modelMapper.map(savedMessageEntity, ChatMessageDto.class);
    }

    @Override
    public List<ChatMessageDto> getUserChatMessages(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new NotFoundException("Not Found User");

        List<ChatMessageEntity> chatMessages = chatMessageRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return chatMessages.stream()
                .map(messageEntity -> modelMapper.map(messageEntity, ChatMessageDto.class))
                .collect(Collectors.toList());
    }
}
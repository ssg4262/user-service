package com.oxog.userservice.repository.chat;

import com.oxog.userservice.Entity.chat.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findAllByOrderByCreatedAtAsc();
    List<ChatMessageEntity> findAllByUserIdOrderByCreatedAtAsc(String userId);
    List<ChatMessageEntity> findByUserIdOrderByCreatedAtDesc(String userId);
}

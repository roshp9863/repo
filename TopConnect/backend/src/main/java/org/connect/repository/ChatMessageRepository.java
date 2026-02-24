package org.connect.repository;

import org.connect.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    Page<ChatMessage> findByThreadIdOrderByCreatedAtDesc(UUID threadId, Pageable pageable);

    List<ChatMessage> findByThreadIdOrderByCreatedAtAsc(UUID threadId);

    long countByThreadIdAndReadAtIsNull(UUID threadId);

    List<ChatMessage> findByThreadIdAndReadAtIsNullAndSenderIdNot(UUID threadId, UUID senderId);

}


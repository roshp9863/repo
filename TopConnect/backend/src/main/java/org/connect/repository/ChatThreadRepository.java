package org.connect.repository;

import org.connect.entity.ChatThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatThreadRepository extends JpaRepository<ChatThread, UUID> {

    Optional<ChatThread> findByArtistIdAndUserId(UUID artistId, UUID userId);

    Page<ChatThread> findByArtistIdOrderByUpdatedAtDesc(UUID artistId, Pageable pageable);

    Page<ChatThread> findByUserIdOrderByUpdatedAtDesc(UUID userId, Pageable pageable);

    Optional<ChatThread> findByIdAndArtistId(UUID threadId, UUID artistId);

    Optional<ChatThread> findByIdAndUserId(UUID threadId, UUID userId);

    Optional<ChatThread> findTopByArtistIdOrderByUpdatedAtDesc(UUID artistId);
}


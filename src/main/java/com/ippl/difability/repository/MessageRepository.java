package com.ippl.difability.repository;

import com.ippl.difability.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    
    // hitung pesan belum dibaca
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId AND m.sender.id != :userId AND m.isRead = false")
    Long countUnreadMessages(@Param("conversationId") Long conversationId, @Param("userId") Long userId);
    
    // label sudah di baca
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Message m SET m.isRead = true WHERE m.conversation.id = :conversationId AND m.sender.id != :userId AND m.isRead = false")
    void markMessagesAsRead(@Param("conversationId") Long conversationId, @Param("userId") Long userId);
    
    // pesan terbaru
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt DESC")
    List<Message> findLatestMessagesByConversationId(@Param("conversationId") Long conversationId, Pageable pageable);
    
    default Message findLatestMessageByConversationId(Long conversationId) {
        List<Message> messages = findLatestMessagesByConversationId(conversationId, Pageable.ofSize(1));
        return messages.isEmpty() ? null : messages.get(0);
    }
}

package com.ippl.difability.repository;

import com.ippl.difability.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

    List<Conversation> findByJobSeekerIdOrderByLastMessageDesc(Long jobSeekerId);

    List<Conversation> findByCompanyIdOrderByLastMessageDesc(Long companyId);

    List<Conversation> findByHrUserIdOrderByLastMessageDesc(Long hrUserId);

    @Query("SELECT c FROM Conversation c " +
           "WHERE c.jobSeeker.id = :userId OR c.company.id = :userId OR c.hrUser.id = :userId " +
           "ORDER BY c.lastMessage DESC")
    List<Conversation> findByUserIdOrderByLastMessageDesc(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT m.conversation) FROM Message m " +
           "WHERE m.conversation.id IN (" +
           "  SELECT c.id FROM Conversation c " +
           "  WHERE c.jobSeeker.id = :userId OR c.company.id = :userId OR c.hrUser.id = :userId" +
           ") AND m.sender.id != :userId AND m.isRead = false")
    Long countUnreadConversations(@Param("userId") Long userId);
}

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
    
    // Cari percakapan berdasarkan job dan job seeker
    Optional<Conversation> findByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

    // Semua percakapan job seeker, terbaru di atas
    List<Conversation> findByJobSeekerIdOrderByLastMessageDesc(Long jobSeekerId);

    // Semua percakapan company, terbaru di atas
    List<Conversation> findByCompanyIdOrderByLastMessageDesc(Long companyId);

    // Semua percakapan HR, terbaru di atas
    List<Conversation> findByHrUserIdOrderByLastMessageDesc(Long hrUserId);

    // Semua percakapan untuk user tertentu (jobSeeker / company / hrUser)
    @Query("SELECT c FROM Conversation c " +
           "WHERE c.jobSeeker.id = :userId OR c.company.id = :userId OR c.hrUser.id = :userId " +
           "ORDER BY c.lastMessage DESC")
    List<Conversation> findByUserIdOrderByLastMessageDesc(@Param("userId") Long userId);

    // Hitung jumlah percakapan yang belum dibaca
    @Query("SELECT COUNT(DISTINCT m.conversation) FROM Message m " +
           "WHERE m.conversation.id IN (" +
           "  SELECT c.id FROM Conversation c " +
           "  WHERE c.jobSeeker.id = :userId OR c.company.id = :userId OR c.hrUser.id = :userId" +
           ") AND m.sender.id != :userId AND m.isRead = false")
    Long countUnreadConversations(@Param("userId") Long userId);
}

package com.ippl.difability.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long companyId;
    private String companyName;
    private Long jobSeekerId;
    private String jobSeekerName;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime lastMessageAt;
    private String lastMessageContent;
    private Long unreadCount;
    private List<MessageResponse> recentMessages;
}
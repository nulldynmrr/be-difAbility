package com.ippl.difability.service;

import com.ippl.difability.dto.request.CreateConversationRequest;
import com.ippl.difability.dto.request.SendMessageRequest;
import com.ippl.difability.dto.response.ConversationResponse;
import com.ippl.difability.dto.response.MessageResponse;
import com.ippl.difability.entity.*;
import com.ippl.difability.repository.*;
import com.ippl.difability.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Transactional
    public ConversationResponse createOrGetConversation(CreateConversationRequest request, String username) {
 
        if (request.getJobId() == null || request.getJobSeekerId() == null) {
            throw new IllegalArgumentException("Job ID and Job Seeker ID are required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long currentUserId = user.getId();

        // Cek apakah conversation sudah ada
        Conversation conversation = conversationRepository
                .findByJobIdAndJobSeekerId(request.getJobId(), request.getJobSeekerId())
                .orElseGet(() -> {
                    Conversation newConv = new Conversation();

                    Job job = jobRepository.findById(request.getJobId())
                            .orElseThrow(() -> new ResourceNotFoundException("Job", "id", request.getJobId()));
                    
                    User jobSeeker = userRepository.findById(request.getJobSeekerId())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getJobSeekerId()));

                    newConv.setJob(job);
                    newConv.setJobSeeker(jobSeeker);
                    newConv.setCompany(job.getCompany());
                    newConv.setHrUser(user);
                    newConv.setStatus("ACTIVE");
                    newConv.setStartedAt(LocalDateTime.now());
                    newConv.setLastMessage(LocalDateTime.now());

                    return conversationRepository.save(newConv);
                });

        // Kirim initial message jika ada
        if (request.getInitialMessage() != null && !request.getInitialMessage().trim().isEmpty()) {
            SendMessageRequest messageRequest = new SendMessageRequest();
            messageRequest.setConversationId(conversation.getId());
            messageRequest.setMessageContent(request.getInitialMessage());
            sendMessage(messageRequest, username);
        }

        return mapToConversationResponse(conversation, currentUserId);
    }

    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request, String username) {
        // Validasi input
        if (request.getConversationId() == null) {
            throw new IllegalArgumentException("Conversation ID is required");
        }
        if (request.getMessageContent() == null || request.getMessageContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long senderId = user.getId();

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", request.getConversationId()));

        // Validasi apakah user adalah participant
        if (!isParticipant(conversation, senderId)) {
            throw new IllegalArgumentException("You are not a participant in this conversation");
        }

        // Buat message baru
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(user);
        message.setMessageContent(request.getMessageContent().trim());
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        message = messageRepository.save(message);

        // Update last message timestamp di conversation
        conversation.setLastMessage(LocalDateTime.now());
        conversationRepository.save(conversation);

        return mapToMessageResponse(message);
    }

    @Transactional
    public List<MessageResponse> getMessages(Long conversationId, String username) {
        if (conversationId == null) {
            throw new IllegalArgumentException("Conversation ID is required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long currentUserId = user.getId();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));

        if (!isParticipant(conversation, currentUserId)) {
            throw new IllegalArgumentException("You are not a participant in this conversation");
        }

        // Mark messages sebagai sudah dibaca
        messageRepository.markMessagesAsRead(conversationId, currentUserId);

        // Ambil semua messages
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);

        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long userId = user.getId();

        List<Conversation> conversations = conversationRepository.findByUserIdOrderByLastMessageDesc(userId);

        return conversations.stream()
                .map(conv -> mapToConversationListResponse(conv, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversation(Long conversationId, String username) {
        if (conversationId == null) {
            throw new IllegalArgumentException("Conversation ID is required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long currentUserId = user.getId();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));

        if (!isParticipant(conversation, currentUserId)) {
            throw new IllegalArgumentException("You are not a participant in this conversation");
        }

        return mapToConversationResponse(conversation, currentUserId);
    }

    // Helper method untuk cek apakah user adalah participant
    private boolean isParticipant(Conversation conversation, Long userId) {
        if (userId == null) {
            return false;
        }
        
        boolean isJobSeeker = conversation.getJobSeeker() != null && 
                             conversation.getJobSeeker().getId().equals(userId);
        boolean isCompany = conversation.getCompany() != null && 
                           conversation.getCompany().getId().equals(userId);
        boolean isHrUser = conversation.getHrUser() != null && 
                          conversation.getHrUser().getId().equals(userId);
        
        return isJobSeeker || isCompany || isHrUser;
    }

    // Mapping ke MessageResponse
    private MessageResponse mapToMessageResponse(Message message) {
        if (message == null) {
            return null;
        }

        return new MessageResponse(
                message.getId(),
                message.getConversation() != null ? message.getConversation().getId() : null,
                message.getSender() != null ? message.getSender().getId() : null,
                message.getSender() != null ? message.getSender().getUsername() : "Unknown",
                message.getSender() != null && message.getSender().getRole() != null ? 
                    message.getSender().getRole().name() : "UNKNOWN",
                message.getMessageContent(),
                message.getCreatedAt(),
                message.getIsRead()
        );
    }

    // Mapping ke ConversationResponse dengan messages
    private ConversationResponse mapToConversationResponse(Conversation conversation, Long currentUserId) {
        Message latestMessage = messageRepository.findLatestMessageByConversationId(conversation.getId());
        Long unreadCount = messageRepository.countUnreadMessages(conversation.getId(), currentUserId);
        List<Message> recentMessages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());

        return new ConversationResponse(
                conversation.getId(),
                conversation.getJob() != null ? conversation.getJob().getId() : null,
                conversation.getJob() != null ? conversation.getJob().getTitle() : "Unknown Job",
                conversation.getCompany() != null ? conversation.getCompany().getId() : null,
                conversation.getCompany() != null ? conversation.getCompany().getUsername() : "Unknown Company",
                conversation.getJobSeeker() != null ? conversation.getJobSeeker().getId() : null,
                conversation.getJobSeeker() != null ? conversation.getJobSeeker().getUsername() : "Unknown Job Seeker",
                conversation.getStatus(),
                conversation.getStartedAt(),
                conversation.getLastMessage(),
                latestMessage != null ? latestMessage.getMessageContent() : null,
                unreadCount != null ? unreadCount : 0L,
                recentMessages.stream()
                        .map(this::mapToMessageResponse)
                        .collect(Collectors.toList())
        );
    }

    // Mapping ke ConversationResponse tanpa messages (untuk list)
    private ConversationResponse mapToConversationListResponse(Conversation conversation, Long currentUserId) {
        Message latestMessage = messageRepository.findLatestMessageByConversationId(conversation.getId());
        Long unreadCount = messageRepository.countUnreadMessages(conversation.getId(), currentUserId);

        return new ConversationResponse(
                conversation.getId(),
                conversation.getJob() != null ? conversation.getJob().getId() : null,
                conversation.getJob() != null ? conversation.getJob().getTitle() : "Unknown Job",
                conversation.getCompany() != null ? conversation.getCompany().getId() : null,
                conversation.getCompany() != null ? conversation.getCompany().getUsername() : "Unknown Company",
                conversation.getJobSeeker() != null ? conversation.getJobSeeker().getId() : null,
                conversation.getJobSeeker() != null ? conversation.getJobSeeker().getUsername() : "Unknown Job Seeker",
                conversation.getStatus(),
                conversation.getStartedAt(),
                conversation.getLastMessage(),
                latestMessage != null ? latestMessage.getMessageContent() : null,
                unreadCount != null ? unreadCount : 0L,
                null // Tidak include messages untuk list
        );
    }
}
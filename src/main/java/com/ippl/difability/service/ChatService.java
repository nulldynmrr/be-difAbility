package com.ippl.difability.service;

import com.ippl.difability.dto.*;
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
        User user = userRepository.findByIdentifier(username).orElseThrow(() -> new ResourceNotFoundException("message sembarang"));
        Long currentUserId = user.getId();
        Conversation conversation = conversationRepository
                .findByJobIdAndJobSeekerId(request.getJobId(), request.getJobSeekerId())
                .orElseGet(() -> {
                    Conversation newConv = new Conversation();

                    Job job = jobRepository.findById(request.getJobId())
                            .orElseThrow(() -> new RuntimeException("Job not found"));
                    User jobSeeker = userRepository.findById(request.getJobSeekerId())
                            .orElseThrow(() -> new RuntimeException("Job seeker not found"));

                    newConv.setJob(job);
                    newConv.setJobSeeker(jobSeeker);
                    newConv.setCompany(job.getCompany());
                    newConv.setStatus("ACTIVE");
                    newConv.setStartedAt(LocalDateTime.now());

                    return conversationRepository.save(newConv);
                });

        if (request.getInitialMessage() != null && !request.getInitialMessage().isEmpty()) {
            SendMessageRequest messageRequest = new SendMessageRequest(conversation.getId(), request.getInitialMessage());
            sendMessage(messageRequest, username);
        }

        return mapToConversationResponse(conversation, currentUserId);
    }

    // Kirim pesan
    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request, String username) {
        User user = userRepository.findByIdentifier(username).orElseThrow(() -> new ResourceNotFoundException("message sembarang"));
        Long senderId = user.getId();     
       
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        if (!isParticipant(conversation, senderId)) {
            throw new RuntimeException("You are not a participant in this conversation");
        }

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageContent(request.getMessageContent());
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        message = messageRepository.save(message);

        conversation.setLastMessage(LocalDateTime.now());
        conversationRepository.save(conversation);

        return mapToMessageResponse(message);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(Long conversationId, String username) {
        User user = userRepository.findByIdentifier(username).orElseThrow(() -> new ResourceNotFoundException("message sembarang"));
        Long currentUserId = user.getId();
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!isParticipant(conversation, currentUserId)) {
            throw new RuntimeException("You are not a participant in this conversation");
        }

        messageRepository.markMessagesAsRead(conversationId, currentUserId);

        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);

        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(String username) {
        User user = userRepository.findByIdentifier(username).orElseThrow(() -> new ResourceNotFoundException("message sembarang"));
        Long userId = user.getId();
        List<Conversation> conversations = conversationRepository.findByUserIdOrderByLastMessageAtDesc(userId);

        return conversations.stream()
                .map(conv -> mapToConversationListResponse(conv, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversation(Long conversationId, String username) {
        User user = userRepository.findByIdentifier(username).orElseThrow(() -> new ResourceNotFoundException("message sembarang"));
        Long currentUserId = user.getId();
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!isParticipant(conversation, currentUserId)) {
            throw new RuntimeException("You are not a participant in this conversation");
        }

        return mapToConversationResponse(conversation, currentUserId);
    }

    private boolean isParticipant(Conversation conversation, Long userId) {
        return conversation.getJobSeeker().getId().equals(userId) ||
               conversation.getCompany().getId().equals(userId) ||
               (conversation.getHrUser() != null && conversation.getHrUser().getId().equals(userId));
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getIdentifier(),
                message.getSender().getRole().name(),
                message.getMessageContent(),
                message.getCreatedAt(),
                message.getIsRead()
        );
    }

    private ConversationResponse mapToConversationResponse(Conversation conversation, Long currentUserId) {
        Message latestMessage = messageRepository.findLatestMessageByConversationId(currentUserId);
        Long unreadCount = messageRepository.countUnreadMessages(conversation.getId(), currentUserId);
        List<Message> recentMessages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());

        return new ConversationResponse(
                conversation.getId(),
                conversation.getJob().getId(),
                conversation.getJob().getTitle(),
                conversation.getCompany().getId(),
                conversation.getCompany().getIdentifier(),
                conversation.getJobSeeker().getId(),
                conversation.getJobSeeker().getIdentifier(),
                conversation.getStatus(),
                conversation.getStartedAt(),
                conversation.getLastMessage(),
                latestMessage != null ? latestMessage.getMessageContent() : null,
                unreadCount,
                recentMessages.stream().map(this::mapToMessageResponse).collect(Collectors.toList())
        );
    }

    private ConversationResponse mapToConversationListResponse(Conversation conversation, Long currentUserId) {
        Message latestMessage = messageRepository.findLatestMessageByConversationId(conversation.getId());
        Long unreadCount = messageRepository.countUnreadMessages(conversation.getId(), currentUserId);

        return new ConversationResponse(
        conversation.getId(),
        conversation.getJob().getId(),
        conversation.getJob().getTitle(),
        conversation.getCompany().getId(),
        conversation.getCompany().getIdentifier(),
        conversation.getJobSeeker().getId(),
        conversation.getJobSeeker().getIdentifier(),
        conversation.getStatus(),
        conversation.getStartedAt(),
        conversation.getLastMessage(),
        latestMessage != null ? latestMessage.getMessageContent() : null,
        unreadCount,

        null
);

    }
}
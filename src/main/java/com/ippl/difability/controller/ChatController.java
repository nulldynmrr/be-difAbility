package com.ippl.difability.controller;

import com.ippl.difability.dto.*;
import com.ippl.difability.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping("/conversations")
    public ResponseEntity<ConversationResponse> createOrGetConversation(
            @RequestBody CreateConversationRequest request,
            Principal principal) {
        ConversationResponse response = chatService.createOrGetConversation(request, principal.getName());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getUserConversations(
            Principal principal) {
        List<ConversationResponse> conversations = chatService.getUserConversations(principal.getName());
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationResponse> getConversation(
            @PathVariable Long conversationId,
            Principal principal){
        ConversationResponse response = chatService.getConversation(conversationId, principal.getName());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestBody SendMessageRequest request,
            Principal principal) {
        MessageResponse response = chatService.sendMessage(request, principal.getName());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Long conversationId,
            Authentication authentication) {
        List<MessageResponse> messages = chatService.getMessages(conversationId, principal.getName());
        return ResponseEntity.ok(messages);
    }
}
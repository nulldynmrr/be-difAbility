package com.ippl.difability.controller;

import com.ippl.difability.dto.request.CreateConversationRequest;
import com.ippl.difability.dto.request.SendMessageRequest;
import com.ippl.difability.dto.response.ConversationResponse;
import com.ippl.difability.dto.response.MessageResponse;
import com.ippl.difability.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatService chatService;
    
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/conversations")
    public ResponseEntity<ConversationResponse> createOrGetConversation(
            @Valid @RequestBody CreateConversationRequest request,
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        ConversationResponse response = chatService.createOrGetConversation(
            request, 
            principal.getName()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getUserConversations(
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<ConversationResponse> conversations = chatService.getUserConversations(
            principal.getName()
        );
        return ResponseEntity.ok(conversations);
    }


    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationResponse> getConversation(
            @PathVariable Long conversationId,
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        ConversationResponse response = chatService.getConversation(
            conversationId, 
            principal.getName()
        );
        return ResponseEntity.ok(response);
    }
    

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest request,
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        MessageResponse response = chatService.sendMessage(
            request, 
            principal.getName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Long conversationId,
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<MessageResponse> messages = chatService.getMessages(
            conversationId, 
            principal.getName()
        );
        return ResponseEntity.ok(messages);
    }
}
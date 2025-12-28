package com.ippl.difability.controller;

import com.ippl.difability.dto.response.MessageResponse;
import com.ippl.difability.dto.request.SendMessageRequest;
import com.ippl.difability.dto.TypingNotification;
import com.ippl.difability.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
public class WebSocketChatController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public WebSocketChatController(ChatService chatService, 
                                  SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        if (request == null || request.getConversationId() == null) {
            throw new IllegalArgumentException("Invalid message request");
        }
        
        MessageResponse message = chatService.sendMessage(request, principal.getName());
    
        messagingTemplate.convertAndSend(
            "/topic/conversation/" + request.getConversationId(), 
            message
        );
    }
    
    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload TypingNotification notification, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        if (notification == null || notification.getConversationId() == null) {
            throw new IllegalArgumentException("Invalid typing notification");
        }
        
        messagingTemplate.convertAndSend(
            "/topic/conversation/" + notification.getConversationId() + "/typing",
            notification
        );
    }
}
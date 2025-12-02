package com.ippl.difability.controller;

import com.ippl.difability.dto.MessageResponse;
import com.ippl.difability.dto.SendMessageRequest;
import com.ippl.difability.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    
    // Mengirim pesan melalui WebSocket
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        // Dapatkan user ID dari principal
        Long senderId = getUserIdFromPrincipal(principal);
        
        // Simpan pesan ke database
        MessageResponse message = chatService.sendMessage(request, senderId);
        
        // Kirim pesan ke semua peserta percakapan
        // Format: /topic/conversation/{conversationId}
        messagingTemplate.convertAndSend(
            "/topic/conversation/" + request.getConversationId(), 
            message
        );
    }
    
    // Notifikasi typing indicator
    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload TypingNotification notification, Principal principal) {
        messagingTemplate.convertAndSend(
            "/topic/conversation/" + notification.getConversationId() + "/typing",
            notification
        );
    }
    
    private Long getUserIdFromPrincipal(Principal principal) {
        // Implementasi untuk mendapatkan user ID dari principal
        // Sesuaikan dengan sistem authentication 
        return 1L; 
    }
}

class TypingNotification {
    private Long conversationId;
    private Long userId;
    private String userName;
    private boolean isTyping;
    
    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public boolean isTyping() { return isTyping; }
    public void setTyping(boolean typing) { isTyping = typing; }
}
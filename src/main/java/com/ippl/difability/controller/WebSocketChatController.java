package com.ippl.difability.controller;

import com.ippl.difability.dto.MessageResponse;
import com.ippl.difability.dto.SendMessageRequest;
import com.ippl.difability.dto.TypingNotification;
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
        // Simpan pesan ke database menggunakan username dari principal
        MessageResponse message = chatService.sendMessage(request, principal.getName());
        
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
    
}

package com.ippl.difability.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypingNotification {
    private Long conversationId;
    private Long userId;
    private String userName;
    private boolean isTyping;
}

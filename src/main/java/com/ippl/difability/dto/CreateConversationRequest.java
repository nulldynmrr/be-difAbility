package com.ippl.difability.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationRequest {
    private Long jobId;
    private Long jobSeekerId;
    private String initialMessage;
}

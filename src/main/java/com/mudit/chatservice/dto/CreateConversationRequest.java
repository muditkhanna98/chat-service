package com.mudit.chatservice.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateConversationRequest(
        @NotEmpty List<String> participantSubs
) {
}

package com.mudit.chatservice.dto;

import com.mudit.chatservice.entities.Conversation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ConversationResponse(
        UUID id,
        List<String> participantSubs,
        OffsetDateTime createdAt
) {
    public static ConversationResponse from(Conversation conversation, List<String> participantSubs) {
        return new ConversationResponse(
                conversation.getId(),
                participantSubs,
                conversation.getCreatedAt()
        );
    }
}

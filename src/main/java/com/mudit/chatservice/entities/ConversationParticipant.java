package com.mudit.chatservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "conversation_participants")
public class ConversationParticipant {

    @EmbeddedId
    private ConversationParticipantId id;

    @MapsId("conversationId")
    @ManyToOne()
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(name = "joined_at", updatable = false, nullable = false)
    private OffsetDateTime joinedAt;

    @ManyToOne()
    @JoinColumn(name = "last_read_message_id")
    private Message lastReadMessage;

    @PrePersist
    void onCreate() {
        joinedAt = OffsetDateTime.now();
    }
}

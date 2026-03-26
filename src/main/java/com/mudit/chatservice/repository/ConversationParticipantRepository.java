package com.mudit.chatservice.repository;

import com.mudit.chatservice.entities.ConversationParticipant;
import com.mudit.chatservice.entities.ConversationParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipantId> {
}

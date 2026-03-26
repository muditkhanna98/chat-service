package com.mudit.chatservice.service;

import com.mudit.chatservice.dto.ConversationResponse;
import com.mudit.chatservice.dto.CreateConversationRequest;
import com.mudit.chatservice.entities.Conversation;
import com.mudit.chatservice.entities.ConversationParticipant;
import com.mudit.chatservice.entities.ConversationParticipantId;
import com.mudit.chatservice.repository.ConversationParticipantRepository;
import com.mudit.chatservice.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               ConversationParticipantRepository participantRepository) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional
    public ConversationResponse createConversation(String creatorSub, CreateConversationRequest request) {
        Conversation conversation = new Conversation();
        conversationRepository.save(conversation);

        // Include the creator + all requested participants
        List<String> allParticipantSubs = new ArrayList<>();
        allParticipantSubs.add(creatorSub);
        allParticipantSubs.addAll(request.participantSubs());

        for (String userSub : allParticipantSubs) {
            ConversationParticipantId participantId = new ConversationParticipantId();
            participantId.setConversationId(conversation.getId());
            participantId.setUserId(userSub);

            ConversationParticipant participant = new ConversationParticipant();
            participant.setId(participantId);
            participant.setConversation(conversation);
            participantRepository.save(participant);
        }

        return ConversationResponse.from(conversation, allParticipantSubs);
    }
}

package com.mudit.chatservice.controller;

import com.mudit.chatservice.dto.ConversationResponse;
import com.mudit.chatservice.dto.CreateConversationRequest;
import com.mudit.chatservice.security.CurrentUser;
import com.mudit.chatservice.security.GatewayAuthenticationFilter;
import com.mudit.chatservice.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResponse createConversation(
            @RequestAttribute(GatewayAuthenticationFilter.REQUEST_ATTRIBUTE_CURRENT_USER) CurrentUser currentUser,
            @RequestBody @Valid CreateConversationRequest request) {
        return conversationService.createConversation(currentUser.sub(), request);
    }
}

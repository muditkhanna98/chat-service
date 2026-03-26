package com.mudit.chatservice.security;

public record CurrentUser(String sub, String email, String name) {
}

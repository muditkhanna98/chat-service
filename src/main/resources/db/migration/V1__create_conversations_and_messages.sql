CREATE TABLE conversations (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE conversation_participants (
    conversation_id UUID NOT NULL REFERENCES conversations (id) ON DELETE CASCADE,
    user_id UUID NOT NULL,
    joined_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_read_message_id UUID NULL,
    PRIMARY KEY (conversation_id, user_id)
);

CREATE TABLE messages (
    id UUID PRIMARY KEY,
    conversation_id UUID NOT NULL REFERENCES conversations (id) ON DELETE CASCADE,
    sender_user_id UUID NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE conversation_participants
    ADD CONSTRAINT fk_conversation_participants_last_read_message
        FOREIGN KEY (last_read_message_id)
            REFERENCES messages (id)
            ON DELETE SET NULL;

CREATE INDEX idx_conversation_participants_user_id
    ON conversation_participants (user_id);

CREATE INDEX idx_messages_conversation_id_created_at
    ON messages (conversation_id, created_at DESC);

CREATE INDEX idx_messages_sender_user_id
    ON messages (sender_user_id);

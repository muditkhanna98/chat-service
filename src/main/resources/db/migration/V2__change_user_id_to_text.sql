ALTER TABLE conversation_participants
    DROP CONSTRAINT conversation_participants_pkey;

ALTER TABLE conversation_participants
    ALTER COLUMN user_id TYPE TEXT USING user_id::TEXT;

ALTER TABLE conversation_participants
    ADD PRIMARY KEY (conversation_id, user_id);

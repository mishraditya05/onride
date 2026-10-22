CREATE TABLE chat_sessions (
    id          UUID         PRIMARY KEY,
    rider_id    UUID         NOT NULL,
    status      VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) WITH TIME ZONE,
    closed_at   TIMESTAMP(6) WITH TIME ZONE
);

CREATE INDEX idx_chat_sessions_rider_id ON chat_sessions (rider_id);
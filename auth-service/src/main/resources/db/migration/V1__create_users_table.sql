CREATE TABLE users (
    id            UUID         PRIMARY KEY,
    email         VARCHAR(255) UNIQUE,
    phone_number  VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255),
    role          VARCHAR(255) NOT NULL,
    status        VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP(6) WITH TIME ZONE,
    updated_at    TIMESTAMP(6) WITH TIME ZONE
);

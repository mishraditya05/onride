CREATE TABLE riders (
    id               UUID         PRIMARY KEY,
    user_id          UUID         NOT NULL UNIQUE REFERENCES users(id),
    first_name       VARCHAR(255),
    last_name        VARCHAR(255),
    date_of_birth    DATE,
    onboarding_stage VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP(6) WITH TIME ZONE,
    updated_at       TIMESTAMP(6) WITH TIME ZONE
);
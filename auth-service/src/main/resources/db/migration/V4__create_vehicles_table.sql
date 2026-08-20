CREATE TABLE vehicles (
    id            UUID         PRIMARY KEY,
    driver_id     UUID         NOT NULL REFERENCES drivers(id),
    brand         VARCHAR(255) NOT NULL,
    model         VARCHAR(255) NOT NULL,
    year          INTEGER      NOT NULL,
    color         VARCHAR(255) NOT NULL,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    status        VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP(6) WITH TIME ZONE,
    updated_at    TIMESTAMP(6) WITH TIME ZONE
);
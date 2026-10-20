ALTER TABLE rides
    ADD COLUMN matching_mode     VARCHAR(255) NOT NULL DEFAULT 'DIRECT',
    ADD COLUMN broadcast_message VARCHAR(255),
    ADD COLUMN rider_note        VARCHAR(255);

CREATE TABLE rides (
    id               UUID             PRIMARY KEY,
    rider_id         UUID             NOT NULL,
    driver_id        UUID,
    quote_id         UUID             NOT NULL,
    pickup_lat       DOUBLE PRECISION NOT NULL,
    pickup_lng       DOUBLE PRECISION NOT NULL,
    drop_lat         DOUBLE PRECISION NOT NULL,
    drop_lng         DOUBLE PRECISION NOT NULL,
    pickup_geo_cell  VARCHAR(255)     NOT NULL,
    distance_metres  BIGINT           NOT NULL,
    vehicle_type     VARCHAR(255)     NOT NULL,
    fare             NUMERIC(10, 2)   NOT NULL,
    currency         VARCHAR(255)     NOT NULL,
    status           VARCHAR(255)     NOT NULL,
    created_at       TIMESTAMP(6) WITH TIME ZONE,
    updated_at       TIMESTAMP(6) WITH TIME ZONE
);

CREATE INDEX idx_rides_rider_id ON rides (rider_id);
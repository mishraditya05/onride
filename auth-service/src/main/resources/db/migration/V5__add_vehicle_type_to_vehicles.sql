ALTER TABLE vehicles ADD COLUMN vehicle_type VARCHAR(255);

UPDATE vehicles SET vehicle_type = 'SEDAN' WHERE vehicle_type IS NULL;

ALTER TABLE vehicles ALTER COLUMN vehicle_type SET NOT NULL;
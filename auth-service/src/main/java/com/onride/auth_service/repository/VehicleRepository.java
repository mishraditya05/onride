package com.onride.auth_service.repository;

import com.onride.auth_service.entity.Vehicle;
import com.onride.auth_service.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    List<Vehicle> findByDriverId(UUID driverId);

    List<Vehicle> findByStatus(VehicleStatus status);
}
package com.onride.auth_service.repository;

import com.onride.auth_service.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
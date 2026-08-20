package com.onride.auth_service.service;

import com.onride.auth_service.dto.DriverResponseDto;
import com.onride.auth_service.dto.UpdateDriverRequestDto;
import com.onride.auth_service.entity.Driver;
import com.onride.auth_service.enums.DriverOnboardingStage;
import com.onride.auth_service.exception.ResourceNotFoundException;
import com.onride.auth_service.mapper.DriverMapper;
import com.onride.auth_service.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public void createInitialProfile(UUID userId) {
        if (driverRepository.existsByUserId(userId)) {
            return;
        }
        Driver driver = new Driver();
        driver.setUserId(userId);
        driver.setOnboardingStage(DriverOnboardingStage.PROFILE_PENDING);
        driverRepository.save(driver);
    }

    public DriverResponseDto getByUserId(UUID userId) {
        return driverMapper.toResponse(findByUserId(userId));
    }

    public DriverResponseDto updateProfile(UUID userId, UpdateDriverRequestDto request) {
        Driver driver = findByUserId(userId);
        driverMapper.updateEntity(request, driver);
        driver.setOnboardingStage(DriverOnboardingStage.VEHICLE_PENDING);
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    Driver findByUserId(UUID userId) {
        return driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found for user: " + userId));
    }
}
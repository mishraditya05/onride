package com.onride.auth_service.service;

import com.onride.auth_service.dto.CreateVehicleRequestDto;
import com.onride.auth_service.dto.VehicleResponseDto;
import com.onride.auth_service.entity.Driver;
import com.onride.auth_service.entity.Vehicle;
import com.onride.auth_service.enums.DriverOnboardingStage;
import com.onride.auth_service.mapper.VehicleMapper;
import com.onride.auth_service.repository.DriverRepository;
import com.onride.auth_service.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final DriverService driverService;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponseDto addVehicle(UUID userId, CreateVehicleRequestDto request) {
        Driver driver = driverService.findByUserId(userId);

        Vehicle vehicle = vehicleMapper.fromDto(request);
        vehicle.setDriverId(driver.getId());
        Vehicle saved = vehicleRepository.save(vehicle);

        driver.setOnboardingStage(DriverOnboardingStage.VERIFICATION_PENDING);
        driverRepository.save(driver);

        return vehicleMapper.toVehicleResponseDto(saved);
    }

    public List<VehicleResponseDto> listMyVehicles(UUID userId) {
        Driver driver = driverService.findByUserId(userId);
        return vehicleRepository.findByDriverId(driver.getId())
                .stream()
                .map(vehicleMapper::toVehicleResponseDto)
                .toList();
    }
}
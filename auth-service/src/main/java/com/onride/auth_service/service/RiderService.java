package com.onride.auth_service.service;

import com.onride.auth_service.dto.RiderResponseDto;
import com.onride.auth_service.dto.UpdateRiderRequestDto;
import com.onride.auth_service.entity.Rider;
import com.onride.auth_service.enums.RiderOnboardingStage;
import com.onride.common.web.error.ResourceNotFoundException;
import com.onride.auth_service.mapper.RiderMapper;
import com.onride.auth_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;
    private final RiderMapper riderMapper;

    public void createInitialProfile(UUID userId) {
        if (riderRepository.existsByUserId(userId)) {
            return;
        }
        Rider rider = new Rider();
        rider.setUserId(userId);
        rider.setOnboardingStage(RiderOnboardingStage.PROFILE_PENDING);
        riderRepository.save(rider);
    }

    public RiderResponseDto getByUserId(UUID userId) {
        return riderMapper.toRiderResponseDto(findByUserId(userId));
    }

    public RiderResponseDto updateProfile(UUID userId, UpdateRiderRequestDto request) {
        Rider rider = findByUserId(userId);
        riderMapper.updateFromDto(request, rider);
        rider.setOnboardingStage(RiderOnboardingStage.PROFILE_COMPLETE);
        return riderMapper.toRiderResponseDto(riderRepository.save(rider));
    }

    private Rider findByUserId(UUID userId) {
        return riderRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found for user: " + userId));
    }
}
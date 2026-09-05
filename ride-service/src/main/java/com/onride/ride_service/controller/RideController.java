package com.onride.ride_service.controller;

import com.onride.ride_service.dto.AcceptMatchRequestDto;
import com.onride.ride_service.dto.AcceptMatchResponseDto;
import com.onride.ride_service.dto.BookRideRequestDto;
import com.onride.ride_service.dto.BookRideResponseDto;
import com.onride.ride_service.dto.PendingMatchResponseDto;
import com.onride.ride_service.dto.QuoteRequestDto;
import com.onride.ride_service.dto.QuoteResponseDto;
import com.onride.ride_service.service.QuoteService;
import com.onride.ride_service.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final QuoteService quoteService;
    private final RideService rideService;

    @PostMapping("/quotes")
    public QuoteResponseDto getQuotes(
            @RequestHeader("X-User-Id") UUID riderId,
            @Valid @RequestBody QuoteRequestDto request) {
        return quoteService.getQuotes(riderId, request);
    }

    @PostMapping("/book")
    public BookRideResponseDto book(
            @RequestHeader("X-User-Id") UUID riderId,
            @Valid @RequestBody BookRideRequestDto request) {
        return rideService.book(riderId, request);
    }

    @GetMapping("/matches")
    public PendingMatchResponseDto getMatches(@RequestHeader("X-User-Id") UUID driverId) {
        return rideService.getPendingMatch(driverId);
    }

    @PostMapping("/{rideId}/accept")
    public AcceptMatchResponseDto accept(
            @RequestHeader("X-User-Id") UUID driverId,
            @PathVariable UUID rideId,
            @Valid @RequestBody AcceptMatchRequestDto request) {
        return rideService.acceptMatch(driverId, rideId, request.matchId());
    }
}
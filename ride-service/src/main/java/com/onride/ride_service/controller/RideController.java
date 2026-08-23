package com.onride.ride_service.controller;

import com.onride.ride_service.dto.QuoteRequestDto;
import com.onride.ride_service.dto.QuoteResponseDto;
import com.onride.ride_service.service.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final QuoteService quoteService;

    @PostMapping("/quotes")
    public QuoteResponseDto getQuotes(@Valid @RequestBody QuoteRequestDto request) {
        return quoteService.getQuotes(request);
    }
}
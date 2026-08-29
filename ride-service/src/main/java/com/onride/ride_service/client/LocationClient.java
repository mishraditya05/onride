package com.onride.ride_service.client;

// Feign client, replaced by LocationGrpcClient. Kept for reference.
//
// import com.onride.ride_service.client.dto.NearbyDriverDto;
// import org.springframework.cloud.openfeign.FeignClient;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
//
// import java.util.List;
//
// @FeignClient(name = "location-service")
// public interface LocationClient {
//
//     @GetMapping("/locations/nearby")
//     List<NearbyDriverDto> findNearbyDrivers(@RequestParam double lat,
//                                             @RequestParam double lng,
//                                             @RequestParam int limit);
// }
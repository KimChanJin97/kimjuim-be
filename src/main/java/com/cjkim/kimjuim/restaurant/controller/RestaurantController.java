package com.cjkim.kimjuim.restaurant.controller;

import com.cjkim.kimjuim.restaurant.dto.RestaurantDetailResponse;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/nearby")
    public List<RestaurantNearbyResponse> getRestaurantsNearby(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam(defaultValue = "100") double d,
            @RequestParam(required = false) String[] ex
    ) {
        return restaurantService.findRestaurantsNearby(x, y, d, ex);
    }

    @GetMapping("/{rid}")
    public RestaurantDetailResponse getRestaurantDetail(
            @PathVariable String rid
    ) {
        return restaurantService.findRestaurantDetail(rid);
    }
}

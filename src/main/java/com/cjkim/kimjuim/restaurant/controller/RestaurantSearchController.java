package com.cjkim.kimjuim.restaurant.controller;

import com.cjkim.kimjuim.restaurant.dto.RestaurantAutocompleteResponse;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class RestaurantSearchController {

    private final RestaurantSearchService restaurantSearchService;

    @GetMapping("/restaurants")
    public List<RestaurantNearbyResponse> searchRestaurants(
            @RequestParam String keyword
    ) {
        return restaurantSearchService.searchRestaurants(keyword);
    }

    @GetMapping("/autocomplete")
    public List<RestaurantAutocompleteResponse> autocompleteRestaurants(
            @RequestParam String keyword
    ) {
        return restaurantSearchService.autocompleteRestaurants(keyword);
    }
}
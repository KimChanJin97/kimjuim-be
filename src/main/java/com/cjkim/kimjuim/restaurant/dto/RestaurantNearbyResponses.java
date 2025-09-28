package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;
import java.time.LocalDateTime;
import java.util.List;

public record RestaurantNearbyResponses(
        List<RestaurantNearbyResponse> restaurantNearbyResponses
) {
    public static RestaurantNearbyResponses from(
            List<Restaurant> restaurants,
            LocalDateTime now
    ) {
        List<RestaurantNearbyResponse> restaurantNearbyResponses = restaurants.stream()
                .map(restaurant -> RestaurantNearbyResponse.from(restaurant, now))
                .toList();

        return new RestaurantNearbyResponses(restaurantNearbyResponses);
    }
}

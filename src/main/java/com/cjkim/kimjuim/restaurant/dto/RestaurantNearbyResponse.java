package com.cjkim.kimjuim.restaurant.dto;

import java.util.List;

public record RestaurantNearbyResponse(
        Long id,
        String rid,
        String name,
        double x,
        double y,
        String category,
        String address,
        String recommendedPrice,
        List<String> images,
        String menus,
        String bizHour
) {
}
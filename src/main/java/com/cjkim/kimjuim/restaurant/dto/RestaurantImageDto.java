package com.cjkim.kimjuim.restaurant.dto;

import java.util.Map;

public record RestaurantImageDto(
        Long id,
        String url
) {
    public static RestaurantImageDto from(Map<String, Object> row) {
        return new RestaurantImageDto(
                (Long) row.get("image_id"),
                (String) row.get("image_url")
        );
    }
}
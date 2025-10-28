package com.cjkim.kimjuim.restaurant.dto;

public record RestaurantImageDto(
        Long id,
        String url
) {
    public static RestaurantImageDto of(Long id, String url) {
        return new RestaurantImageDto(id, url);
    }

    public static RestaurantImageDto from(Object[] row, int startIndex) {
        return new RestaurantImageDto(
                (Long) row[startIndex],       // image_id
                (String) row[startIndex + 1]  // image_url
        );
    }

    public static RestaurantImageDto from(Object[] row) {
        return from(row, 14);
    }
}

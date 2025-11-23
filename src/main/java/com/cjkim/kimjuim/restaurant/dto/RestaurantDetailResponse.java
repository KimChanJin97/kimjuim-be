package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;

import java.util.List;

public record RestaurantDetailResponse(
        List<MenuResponse> menus,
        List<ReviewResponse> reviews
) {
    public static RestaurantDetailResponse from(Restaurant restaurant) {
        return new RestaurantDetailResponse(
                restaurant.getMenus().stream().map(MenuResponse::from).toList(),
                restaurant.getReviews().stream().map(ReviewResponse::from).toList()
        );
    }
}

package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;
import com.cjkim.kimjuim.restaurant.domain.RestaurantImage;
import com.cjkim.kimjuim.restaurant.utils.*;

import java.time.LocalDateTime;
import java.util.List;

public record RestaurantNearbyResponse(
        Long id,
        String rid,
        String name,
        double x,
        double y,
        String category,
        String address,
        String roadAddress,
        String recommendedPrice,
        List<String> images,
        String menus,
        String bizHour
) {
    public static RestaurantNearbyResponse from(Restaurant restaurant, LocalDateTime now) {
        return new RestaurantNearbyResponse(
                restaurant.getId(),
                restaurant.getRid(),
                RestaurantUtils.getRestaurant(restaurant),
                restaurant.getCoordinate().getX(),
                restaurant.getCoordinate().getY(),
                CategoryUtils.getCategory(restaurant.getCategory()),
                restaurant.getAddress(),
                restaurant.getRoadAddress(),
                PriceUtils.getRecommendedPrice(restaurant),
                restaurant.getRestaurantImages().stream().map(RestaurantImage::getUrl).toList(),
                MenuUtils.getMenus(restaurant),
                BizHourUtils.getTodayBizHour(restaurant.getBizHours(), now)
        );
    }
}

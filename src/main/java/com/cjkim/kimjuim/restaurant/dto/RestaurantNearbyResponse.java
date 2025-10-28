package com.cjkim.kimjuim.restaurant.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record RestaurantNearbyResponse(
        Long id,
        String rid,
        String name,
        double x,
        double y,
        String category,
        String address,
        String roadAddress,
        String recommendedPrice,    // 전처리된 가격
        List<String> images,        // URL 문자열 리스트
        String menus,               // 전처리된 메뉴 문자열
        String bizHour              // 전처리된 영업시간
) {
    public static RestaurantNearbyResponse from(Map<String, Object> row) {
        return new RestaurantNearbyResponse(
                (Long) row.get("id"),
                (String) row.get("rid"),
                (String) row.get("name"),
                ((Number) row.get("x")).doubleValue(),
                ((Number) row.get("y")).doubleValue(),
                (String) row.get("category"),
                (String) row.get("address"),
                (String) row.get("road_address"),
                null,
                new ArrayList<>(),
                null,
                null
        );
    }
}
package com.cjkim.kimjuim.restaurant.dto;

import java.util.ArrayList;
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
        String recommendedPrice,    // 전처리된 가격
        List<String> images,        // URL 문자열 리스트
        String menus,               // 전처리된 메뉴 문자열
        String bizHour              // 전처리된 영업시간
) {
    public static RestaurantNearbyResponse from(Object[] row) {
        return new RestaurantNearbyResponse(
                (Long) row[0],
                (String) row[1],
                (String) row[2],
                ((Number) row[3]).doubleValue(),
                ((Number) row[4]).doubleValue(),
                (String) row[5],
                (String) row[6],
                (String) row[7],
                null,  // recommendedPrice - 나중에 설정
                new ArrayList<>(),  // images
                null,  // menus - 나중에 설정
                null   // bizHour - 나중에 설정
        );
    }
}
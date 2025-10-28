package com.cjkim.kimjuim.restaurant.dto;

import java.util.Map;

public record MenuDto(
        Long id,
        String name,
        String price,
        String description,
        boolean isRecommended,
        int menuIdx
) {
    public static MenuDto from(Map<String, Object> row) {
        return new MenuDto(
                (Long) row.get("menu_id"),
                (String) row.get("menu_name"),
                (String) row.get("price"),
                (String) row.get("description"),
                (Boolean) row.get("is_recommended"),
                (Integer) row.get("menu_idx")
        );
    }
}
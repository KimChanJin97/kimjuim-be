package com.cjkim.kimjuim.restaurant.dto;

public record MenuDto(
        Long id,
        String name,
        String price,
        String description,
        boolean isRecommended,
        int menuIdx
) {
    public static MenuDto of(
            Long id,
            String name,
            String price,
            String description,
            boolean isRecommended,
            int menuIdx
    ) {
        return new MenuDto(id, name, price, description, isRecommended, menuIdx);
    }

    public static MenuDto from(Object[] row, int startIndex) {
        return new MenuDto(
                (Long) row[startIndex],           // menu_id
                (String) row[startIndex + 1],     // menu_name
                (String) row[startIndex + 2],     // price
                (String) row[startIndex + 3],     // description
                (Boolean) row[startIndex + 4],    // is_recommended
                (Integer) row[startIndex + 5]     // menu_idx
        );
    }

    public static MenuDto from(Object[] row) {
        return from(row, 8);
    }
}

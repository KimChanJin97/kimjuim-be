package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.Menu;
import com.cjkim.kimjuim.restaurant.utils.DescriptionUtils;
import com.cjkim.kimjuim.restaurant.utils.MenuUtils;
import com.cjkim.kimjuim.restaurant.utils.PriceUtils;

import java.util.List;

public record MenuResponse(
        Long id,
        String name,
        String price,
        boolean isRecommended,
        String description,
        int menuIdx,
        List<MenuImageResponse> menuImages
) {
    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                PriceUtils.formatPrice(menu.getPrice()),
                menu.isRecommended(),
                DescriptionUtils.getDescription(menu.getDescription()),
                menu.getMenuIdx(),
                menu.getMenuImages().stream().map(MenuImageResponse::from).toList()
        );
    }
}

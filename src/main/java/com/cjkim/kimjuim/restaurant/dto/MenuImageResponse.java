package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.MenuImage;

public record MenuImageResponse(
        Long id,
        String url
) {
    public static MenuImageResponse from(MenuImage image) {
        return new MenuImageResponse(
                image.getId(),
                image.getImageUrl()
        );
    }

}

package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.ReviewImage;

public record ReviewImageResponse(
        Long id,
        String thumbnailUrl
) {
    public static ReviewImageResponse from(ReviewImage image) {
        return new ReviewImageResponse(
                image.getId(),
                image.getThumbnailUrl()
        );
    }
}

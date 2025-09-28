package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.domain.Review;
import com.cjkim.kimjuim.restaurant.dto.ReviewResponse;

public class ReviewUtils {

    private static final String OMISSION = "...";

    public static String getContent(Review review) {
        String content = review.getContent();
        if (review.getContent().length() >= 100) {
            content = content.substring(0, 100) + OMISSION;
        }
        return content;
    }
}



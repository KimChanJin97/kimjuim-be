package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.Review;
import java.util.Objects;

import com.cjkim.kimjuim.restaurant.utils.ReviewUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ReviewResponse(
        Long id,
        String title,
        String url,
        String authorName,
        String profileUrl,
        String content,
        String createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getTitle(),
                review.getUrl(),
                review.getAuthorName(),
                review.getProfileUrl(),
                ReviewUtils.getContent(review),
                review.getCreatedAt()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewResponse)) return false;
        ReviewResponse that = (ReviewResponse) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}

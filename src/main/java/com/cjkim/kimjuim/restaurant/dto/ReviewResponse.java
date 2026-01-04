package com.cjkim.kimjuim.restaurant.dto;

import com.cjkim.kimjuim.restaurant.domain.Review;
import java.util.Base64;
import java.util.Objects;

import com.cjkim.kimjuim.restaurant.utils.ReviewUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ReviewResponse(
        Long id,
        String title,
        String url,
        String authorName,
        String profileImage,
        String content,
        String createdAt
) {
    public static ReviewResponse from(Review review) {
        String profileImageBase64 = null;
        if (review.getProfileImage() != null && review.getProfileImage().length > 0) {
            profileImageBase64 = Base64.getEncoder().encodeToString(review.getProfileImage());
        }
        
        return new ReviewResponse(
                review.getId(),
                review.getTitle(),
                review.getUrl(),
                review.getAuthorName(),
                profileImageBase64,
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

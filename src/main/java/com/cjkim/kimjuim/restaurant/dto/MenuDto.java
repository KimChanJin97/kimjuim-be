package com.cjkim.kimjuim.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MenuDto(
        Long id,
        String name,
        String price,
        String description,
        @JsonProperty("isRecommended")
        Boolean isRecommended,
        @JsonProperty("menuIdx")
        Integer menuIdx
) {
    public boolean recommended() {
        return isRecommended != null && isRecommended;
    }
}
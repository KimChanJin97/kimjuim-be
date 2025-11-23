package com.cjkim.kimjuim.restaurant.dto;

public record RestaurantAutocompleteResponse(
        Long id,
        String name,
        String address,
        String category
) {
    public static RestaurantAutocompleteResponse from(
            com.cjkim.kimjuim.restaurant.domain.RestaurantDocument document
    ) {
        return new RestaurantAutocompleteResponse(
                document.getId(),
                document.getName(),
                document.getAddress(),
                document.getCategory()
        );
    }
}
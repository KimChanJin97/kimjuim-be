package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;

public class RestaurantUtils {

    private static final String SPACE = " ";

    public static String getRestaurant(Restaurant restaurant) {
        return restaurant.getName().split(SPACE)[0];
    }
}

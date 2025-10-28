package com.cjkim.kimjuim.restaurant.repository;

import java.util.List;
import java.util.Map;

public interface RestaurantRepositoryCustom {
    List<Map<String, Object>> findRestaurantsNearby(
            double x,
            double y,
            double d,
            String[] ex
    );
}

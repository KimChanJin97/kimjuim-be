package com.cjkim.kimjuim.restaurant.service;

import com.cjkim.kimjuim.restaurant.dto.RestaurantDetailResponse;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponses;
import com.cjkim.kimjuim.restaurant.repository.RestaurantRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional(readOnly = true)
    public RestaurantNearbyResponses findRestaurantsNearby(
            double x,
            double y,
            double distance
    ) {
        return RestaurantNearbyResponses.from(
                restaurantRepository.findRestaurantsNearby(x, y, distance),
                LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public RestaurantDetailResponse findRestaurantDetail(
            String rid
    ) {
        return RestaurantDetailResponse.from(
                restaurantRepository.findRestaurantByRid(rid)
        );
    }
}

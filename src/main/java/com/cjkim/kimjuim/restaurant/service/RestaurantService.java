package com.cjkim.kimjuim.restaurant.service;

import com.cjkim.kimjuim.mapper.RestaurantQueryMapper;
import com.cjkim.kimjuim.restaurant.dto.RestaurantDetailResponse;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantQueryMapper restaurantQueryMapper;

    @Transactional(readOnly = true)
    public List<RestaurantNearbyResponse> findRestaurantsNearby(
            double x,
            double y,
            double d,
            String[] ex
    ) {
        List<Map<String, Object>> queryResults = restaurantRepository.findRestaurantsNearby(x, y, d, ex);
        return restaurantQueryMapper.mapToRestaurants(queryResults, LocalDateTime.now());
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

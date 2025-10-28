package com.cjkim.kimjuim.restaurant.service;

import com.cjkim.kimjuim.mapper.RestaurantQueryMapper;
import com.cjkim.kimjuim.restaurant.dto.RestaurantDetailResponse;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Object[]> queryResults = restaurantRepository.findRestaurantsNearby(x, y, d, ex);
        return restaurantQueryMapper.mapToRestaurantDtos(queryResults, LocalDateTime.now());
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

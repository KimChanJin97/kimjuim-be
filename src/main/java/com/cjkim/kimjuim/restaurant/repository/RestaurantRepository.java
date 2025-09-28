package com.cjkim.kimjuim.restaurant.repository;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    @Query(value = """
    SELECT r.*
    FROM restaurant r
    WHERE ST_DWithin(
          r.coordinate::geography,
          ST_SetSRID(ST_MakePoint(:x,:y), 4326)::geography,
          CAST(:distance AS double precision)
        )
    """, nativeQuery = true)
    List<Restaurant> findRestaurantsNearby(
            @Param("x") double x,
            @Param("y") double y,
            @Param("distance") double distance
    );

    Restaurant findRestaurantByRid(String rid);

}

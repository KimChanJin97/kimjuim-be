package com.cjkim.kimjuim.restaurant.repository;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long>, RestaurantRepositoryCustom {

    @Query("""
    SELECT DISTINCT r FROM Restaurant r
    LEFT JOIN FETCH r.menus m
    LEFT JOIN FETCH m.menuImages
    LEFT JOIN FETCH r.reviews rv
    WHERE r.rid = :rid
    """)
    Restaurant findRestaurantByRid(@Param("rid") String rid);
}

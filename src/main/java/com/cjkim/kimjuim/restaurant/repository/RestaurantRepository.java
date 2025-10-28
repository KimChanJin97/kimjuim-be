package com.cjkim.kimjuim.restaurant.repository;

import com.cjkim.kimjuim.restaurant.domain.Restaurant;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    @Query(value = """
    WITH nearby_restaurants AS (
        SELECT r.id, r.rid, r.name, 
               ST_X(r.coordinate) as x, 
               ST_Y(r.coordinate) as y,
               r.category, r.address, r.road_address,
               r.coordinate  -- ORDER BY에서 사용하기 위해 유지
        FROM restaurant r
        WHERE ST_DWithin(
              r.coordinate::geography,
              ST_SetSRID(ST_MakePoint(:x,:y), 4326)::geography,
              CAST(:d AS double precision)
            )
            AND (CAST(:ex AS text[]) IS NULL OR r.rid <> ALL(CAST(:ex AS text[])))
        ORDER BY r.coordinate <-> ST_SetSRID(ST_MakePoint(:x, :y), 4326)
    )
    SELECT 
        nr.id, nr.rid, nr.name, nr.x, nr.y, nr.category, 
        nr.address, nr.road_address,
        m.id as menu_id, m.name as menu_name, m.price, m.description, 
        m.is_recommended, m.menu_idx,
        ri.id as image_id, ri.url as image_url,
        bh.id as biz_hour_id, bh.day, bh.biz_start, bh.biz_end,
        bh.break_start, bh.break_end, bh.last_order
    FROM nearby_restaurants nr
    LEFT JOIN menu m ON m.restaurant_id = nr.id
    LEFT JOIN restaurant_image ri ON ri.restaurant_id = nr.id  
    LEFT JOIN biz_hour bh ON bh.restaurant_id = nr.id
    ORDER BY nr.x, nr.y, m.menu_idx, bh.day
    """, nativeQuery = true)
    List<Object[]> findRestaurantsNearby(
            @Param("x") double x,
            @Param("y") double y,
            @Param("d") double d,
            @Param("ex") String[] ex
    );

    @Query("""
    SELECT DISTINCT r FROM Restaurant r
    LEFT JOIN FETCH r.menus m
    LEFT JOIN FETCH m.menuImages
    LEFT JOIN FETCH r.reviews rv
    WHERE r.rid = :rid
    """)
    Restaurant findRestaurantByRid(@Param("rid") String rid);
}

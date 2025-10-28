package com.cjkim.kimjuim.restaurant.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findRestaurantsNearby(
            double x,
            double y,
            double d,
            String[] ex
    ) {
        String sql = """
            WITH nearby_restaurants AS (
                SELECT r.id, r.rid, r.name, 
                       ST_X(r.coordinate) as x, 
                       ST_Y(r.coordinate) as y,
                       r.category, r.address, r.road_address,
                       r.coordinate
                FROM restaurant r
                WHERE ST_DWithin(
                      r.coordinate::geography,
                      ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography,
                      CAST(? AS double precision)
                    )
                    AND (CAST(? AS text[]) IS NULL OR r.rid <> ALL(CAST(? AS text[])))
                ORDER BY r.coordinate <-> ST_SetSRID(ST_MakePoint(?, ?), 4326)
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
            """;

        // JdbcTemplate이 자동으로 Map<String, Object>로 변환
        return jdbcTemplate.queryForList(sql, x, y, d, ex, ex, x, y);
    }
}
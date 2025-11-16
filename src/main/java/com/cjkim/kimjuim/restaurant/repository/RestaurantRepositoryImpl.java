package com.cjkim.kimjuim.restaurant.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Map<String, Object>> findRestaurantsNearby(
            double x,
            double y,
            double d,
            String[] ex
    ) {
        String sql = """
            WITH nearby_restaurants AS (
                SELECT 
                    r.id, 
                    r.rid, 
                    r.name, 
                    ST_X(r.coordinate) as x, 
                    ST_Y(r.coordinate) as y,
                    r.category, 
                    r.address, 
                    r.road_address,
                    ST_Distance(
                        r.coordinate::geography,
                        ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography
                    ) as distance
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
                nr.id,
                nr.rid,
                nr.name,
                nr.x,
                nr.y,
                nr.category,
                nr.address,
                nr.road_address,
                nr.distance,
                
                COALESCE(menus.data, '[]'::jsonb)::text as menus,
                COALESCE(images.data, '[]'::jsonb)::text as images,
                COALESCE(biz_hours.data, '[]'::jsonb)::text as biz_hours
                
            FROM nearby_restaurants nr
            
            -- 메뉴 서브쿼리
            LEFT JOIN LATERAL (
                SELECT jsonb_agg(
                    jsonb_build_object(
                        'id', m.id,
                        'name', m.name,
                        'price', m.price,
                        'description', m.description,
                        'isRecommended', m.is_recommended,
                        'menuIdx', m.menu_idx
                    ) ORDER BY m.menu_idx NULLS LAST
                ) as data
                FROM menu m
                WHERE m.restaurant_id = nr.id
            ) menus ON true
            
            -- 이미지 서브쿼리
            LEFT JOIN LATERAL (
                SELECT jsonb_agg(
                    jsonb_build_object(
                        'id', ri.id,
                        'url', ri.url
                    ) ORDER BY ri.id
                ) as data
                FROM restaurant_image ri
                WHERE ri.restaurant_id = nr.id
                LIMIT 10
            ) images ON true
            
            -- 영업시간 서브쿼리
            LEFT JOIN LATERAL (
                SELECT jsonb_agg(
                    jsonb_build_object(
                        'id', bh.id,
                        'day', bh.day,
                        'bizStart', bh.biz_start,
                        'bizEnd', bh.biz_end,
                        'breakStart', bh.break_start,
                        'breakEnd', bh.break_end,
                        'lastOrder', bh.last_order
                    ) ORDER BY bh.day
                ) as data
                FROM biz_hour bh
                WHERE bh.restaurant_id = nr.id
            ) biz_hours ON true
            
            ORDER BY nr.distance
            """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, x, y, x, y, d, ex, ex, x, y);

        return results;
    }

    @Override
    public List<Map<String, Object>> findRestaurantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        String sql = """
        WITH target_restaurants AS (
            SELECT 
                r.id, 
                r.rid, 
                r.name, 
                ST_X(r.coordinate) as x, 
                ST_Y(r.coordinate) as y,
                r.category, 
                r.address,
                r.road_address,
                array_position(?::bigint[], r.id) as sort_order
            FROM restaurant r
            WHERE r.id = ANY(?::bigint[])
        )
        SELECT 
            tr.id::bigint,
            tr.rid,
            tr.name,
            tr.x::double precision,
            tr.y::double precision,
            tr.category,
            tr.address,
            tr.road_address,
            tr.sort_order,
            
            COALESCE(menus.data, '[]'::jsonb)::text as menus,
            COALESCE(images.data, '[]'::jsonb)::text as images,
            COALESCE(biz_hours.data, '[]'::jsonb)::text as biz_hours
            
        FROM target_restaurants tr
        
        -- 메뉴 서브쿼리
        LEFT JOIN LATERAL (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'id', m.id,
                    'name', m.name,
                    'price', m.price,
                    'description', m.description,
                    'isRecommended', m.is_recommended,
                    'menuIdx', m.menu_idx
                ) ORDER BY m.menu_idx NULLS LAST
            ) as data
            FROM menu m
            WHERE m.restaurant_id = tr.id
        ) menus ON true
        
        -- 이미지 서브쿼리
        LEFT JOIN LATERAL (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'id', ri.id,
                    'url', ri.url
                ) ORDER BY ri.id
            ) as data
            FROM restaurant_image ri
            WHERE ri.restaurant_id = tr.id
            LIMIT 10
        ) images ON true
        
        -- 영업시간 서브쿼리
        LEFT JOIN LATERAL (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'id', bh.id,
                    'day', bh.day,
                    'bizStart', bh.biz_start,
                    'bizEnd', bh.biz_end,
                    'breakStart', bh.break_start,
                    'breakEnd', bh.break_end,
                    'lastOrder', bh.last_order
                ) ORDER BY bh.day
            ) as data
            FROM biz_hour bh
            WHERE bh.restaurant_id = tr.id
        ) biz_hours ON true
        
        ORDER BY tr.sort_order NULLS LAST
        """;

        Long[] idsArray = ids.toArray(new Long[0]);
        return jdbcTemplate.queryForList(sql, idsArray, idsArray);
    }
}
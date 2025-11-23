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
        // Geometry 타입 + LATERAL JOIN 최적화
        // - LATERAL JOIN으로 카테시안 곱 방지 (N+1 해결)
        // - ST_Distance로 명시적 거리 계산 후 정렬
        // - GIST 인덱스로 공간 검색 최적화
        // - 5-10% 거리 오차는 음식점 검색에서 허용 가능
        double distanceInDegrees = d / 111000.0;
        
        String sql = """
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
                    r.coordinate,
                    ST_SetSRID(ST_MakePoint(?, ?), 4326)
                ) * 111000.0 as distance,
                
                COALESCE(menus.data, '[]'::jsonb)::text as menus,
                COALESCE(images.data, '[]'::jsonb)::text as images,
                COALESCE(biz_hours.data, '[]'::jsonb)::text as biz_hours
                
            FROM restaurant r
            
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
                WHERE m.restaurant_id = r.id
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
                WHERE ri.restaurant_id = r.id
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
                WHERE bh.restaurant_id = r.id
            ) biz_hours ON true
            
            WHERE ST_DWithin(
                r.coordinate,
                ST_SetSRID(ST_MakePoint(?, ?), 4326),
                ?
            )
            AND (CAST(? AS text[]) IS NULL OR r.rid <> ALL(CAST(? AS text[])))
            
            ORDER BY ST_Distance(
                r.coordinate,
                ST_SetSRID(ST_MakePoint(?, ?), 4326)
            )
            """;

        // 파라미터: x, y (ST_Distance), x, y (WHERE ST_DWithin), distanceInDegrees, ex, ex (제외 목록), x, y (ORDER BY)
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, x, y, x, y, distanceInDegrees, ex, ex, x, y);

        return results;
    }

    @Override
    public List<Map<String, Object>> findRestaurantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        // LATERAL JOIN으로 카테시안 곱 방지
        String sql = """
        SELECT 
            r.id::bigint,
            r.rid,
            r.name,
            ST_X(r.coordinate)::double precision as x,
            ST_Y(r.coordinate)::double precision as y,
            r.category,
            r.address,
            r.road_address,
            array_position(?::bigint[], r.id) as sort_order,
            
            COALESCE(menus.data, '[]'::jsonb)::text as menus,
            COALESCE(images.data, '[]'::jsonb)::text as images,
            COALESCE(biz_hours.data, '[]'::jsonb)::text as biz_hours
            
        FROM restaurant r
        
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
            WHERE m.restaurant_id = r.id
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
            WHERE ri.restaurant_id = r.id
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
            WHERE bh.restaurant_id = r.id
        ) biz_hours ON true
        
        WHERE r.id = ANY(?::bigint[])
        
        ORDER BY array_position(?::bigint[], r.id) NULLS LAST
        """;

        Long[] idsArray = ids.toArray(new Long[0]);
        return jdbcTemplate.queryForList(sql, idsArray, idsArray, idsArray);
    }
}
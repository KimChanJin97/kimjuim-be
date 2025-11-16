package com.cjkim.kimjuim.mapper;

import com.cjkim.kimjuim.restaurant.dto.BizHourDto;
import com.cjkim.kimjuim.restaurant.dto.MenuDto;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.utils.BizHourUtils;
import com.cjkim.kimjuim.restaurant.utils.CategoryUtils;
import com.cjkim.kimjuim.restaurant.utils.MenuUtils;
import com.cjkim.kimjuim.restaurant.utils.PriceUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantQueryMapper {

    private final ObjectMapper objectMapper;

    public List<RestaurantNearbyResponse> mapToRestaurants(
            List<Map<String, Object>> queryResults,
            LocalDateTime now
    ) {
        return queryResults.stream()
                .map(row -> mapToRestaurant(row, now))
                .toList();
    }

    private RestaurantNearbyResponse mapToRestaurant(
            Map<String, Object> row,
            LocalDateTime now
    ) {
        try {
            // 기본 정보
            Long id = ((Number) row.get("id")).longValue();
            String rid = (String) row.get("rid");
            String name = (String) row.get("name");
            Double x = ((Number) row.get("x")).doubleValue();
            Double y = ((Number) row.get("y")).doubleValue();
            String rawCategory = (String) row.get("category");
            String address = (String) row.get("address");

            // JSON 파싱
            String menusJson = (String) row.get("menus");
            String imagesJson = (String) row.get("images");
            String bizHoursJson = (String) row.get("biz_hours");

            List<MenuDto> menus = parseMenus(menusJson);
            List<String> images = parseImages(imagesJson);
            List<BizHourDto> bizHours = parseBizHours(bizHoursJson);

            // Utils 적용
            String category = CategoryUtils.getCategory(rawCategory);
            String recommendedPrice = PriceUtils.getRecommendedPriceFromDto(menus);
            String menuText = MenuUtils.getRepresentativeMenus(menus);
            String bizHour = BizHourUtils.getTodayBizHourFromDto(bizHours, now);

            return new RestaurantNearbyResponse(
                    id, rid, name, x, y, category, address,
                    recommendedPrice, images, menuText, bizHour
            );

        } catch (Exception e) {
            log.error("Failed to map restaurant data: {}", row, e);
            throw new RuntimeException("Restaurant mapping failed", e);
        }
    }

    private List<MenuDto> parseMenus(String json) {
        try {
            if (json == null || json.equals("[]")) {
                return List.of();
            }
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Failed to parse menus JSON: {}", json, e);
            return List.of();
        }
    }

    private List<String> parseImages(String json) {
        try {
            if (json == null || json.equals("[]")) {
                return List.of();
            }

            List<Map<String, Object>> imageList = objectMapper.readValue(
                    json,
                    new TypeReference<>() {}
            );

            return imageList.stream()
                    .map(img -> (String) img.get("url"))
                    .filter(Objects::nonNull)
                    .limit(10)
                    .toList();

        } catch (Exception e) {
            log.error("Failed to parse images JSON: {}", json, e);
            return List.of();
        }
    }

    private List<BizHourDto> parseBizHours(String json) {
        try {
            if (json == null || json.equals("[]")) {
                return List.of();
            }
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Failed to parse biz_hours JSON: {}", json, e);
            return List.of();
        }
    }
}
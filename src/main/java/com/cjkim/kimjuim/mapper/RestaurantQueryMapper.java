package com.cjkim.kimjuim.mapper;

import com.cjkim.kimjuim.restaurant.dto.BizHourDto;
import com.cjkim.kimjuim.restaurant.dto.MenuDto;
import com.cjkim.kimjuim.restaurant.dto.RestaurantImageDto;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.utils.BizHourUtils;
import com.cjkim.kimjuim.restaurant.utils.CategoryUtils;
import com.cjkim.kimjuim.restaurant.utils.MenuUtils;
import com.cjkim.kimjuim.restaurant.utils.PriceUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RestaurantQueryMapper {

    public List<RestaurantNearbyResponse> mapToRestaurantDtos(
            List<Object[]> queryResults,
            LocalDateTime now
    ) {

        Map<Long, TempRestaurantData> restaurantDataMap = new LinkedHashMap<>();

        // 1단계: 데이터 수집
        for (Object[] row : queryResults) {
            Long restaurantId = (Long) row[0];

            TempRestaurantData tempData = restaurantDataMap.computeIfAbsent(
                    restaurantId,
                    id -> {
                        TempRestaurantData data = new TempRestaurantData();
                        data.response = RestaurantNearbyResponse.from(row);
                        data.menus = new ArrayList<>();
                        data.images = new ArrayList<>();
                        data.bizHours = new ArrayList<>();
                        return data;
                    }
            );

            // Menu 정보 추가
            if (row[8] != null) {
                MenuDto menu = MenuDto.from(row);
                if (tempData.menus.stream().noneMatch(m -> m.id().equals(menu.id()))) {
                    tempData.menus.add(menu);
                }
            }

            // RestaurantImage 정보 추가
            if (row[14] != null) {
                RestaurantImageDto image = RestaurantImageDto.from(row);
                if (tempData.images.stream().noneMatch(img -> img.id().equals(image.id()))) {
                    tempData.images.add(image);
                }
            }

            // BizHour 정보 추가
            if (row[16] != null) {
                BizHourDto bizHour = BizHourDto.from(row);
                if (tempData.bizHours.stream().noneMatch(bh -> bh.id().equals(bizHour.id()))) {
                    tempData.bizHours.add(bizHour);
                }
            }
        }

        // 2단계: 기존 Utils 활용하여 최종 응답 생성
        return restaurantDataMap.values().stream()
                .map(tempData -> createFinalResponse(tempData, now))
                .collect(Collectors.toList());
    }

    private RestaurantNearbyResponse createFinalResponse(
            TempRestaurantData tempData, LocalDateTime now) {

        RestaurantNearbyResponse original = tempData.response;

        // 기존 Utils 함수 재사용!
        String categoryFormatted = CategoryUtils.getCategory(original.category());
        String recommendedPrice = PriceUtils.getRecommendedPriceFromDto(tempData.menus);
        String menusFormatted = MenuUtils.getMenusFromDto(tempData.menus);
        String todayBizHour = BizHourUtils.getTodayBizHourFromDto(tempData.bizHours, now);

        List<String> imageUrls = tempData.images.stream()
                .map(RestaurantImageDto::url)
                .collect(Collectors.toList());

        return new RestaurantNearbyResponse(
                original.id(),
                original.rid(),
                original.name(),
                original.x(),
                original.y(),
                categoryFormatted,
                original.address(),
                original.roadAddress(),
                recommendedPrice,
                imageUrls,
                menusFormatted,
                todayBizHour
        );
    }

    // 임시 데이터 클래스
    private static class TempRestaurantData {
        RestaurantNearbyResponse response;
        List<MenuDto> menus;
        List<RestaurantImageDto> images;
        List<BizHourDto> bizHours;
    }
}
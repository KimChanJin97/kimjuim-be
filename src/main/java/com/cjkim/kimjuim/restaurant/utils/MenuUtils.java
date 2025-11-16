package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.dto.MenuDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class MenuUtils {

    private static final int MAX_SIZE = 2;
    private static final String SEPARATOR = ", ";
    private static final String NO_MENU = "정보없음";

    public static String getRepresentativeMenus(List<MenuDto> menus) {
        if (menus == null || menus.isEmpty()) {
            return NO_MENU;
        }

        // 추천 메뉴 2개 먼저 추출 (null 안전)
        String representative = menus.stream()
                .filter(Objects::nonNull)  // null 메뉴 제외
                .filter(MenuUtils::isRecommendedSafely)  // null-safe 추천 체크
                .map(MenuDto::name)
                .filter(Objects::nonNull)  // null 이름 제외
                .filter(name -> !name.trim().isEmpty())  // 빈 이름 제외
                .limit(MAX_SIZE)
                .collect(Collectors.joining(SEPARATOR));

        // 추천 메뉴가 없으면 일반 메뉴 2개
        if (representative.isEmpty()) {
            representative = menus.stream()
                    .filter(Objects::nonNull)  // null 메뉴 제외
                    .map(MenuDto::name)
                    .filter(Objects::nonNull)  // null 이름 제외
                    .filter(name -> !name.trim().isEmpty())  // 빈 이름 제외
                    .limit(MAX_SIZE)
                    .collect(Collectors.joining(SEPARATOR));
        }

        // 그래도 메뉴가 없으면 NO_MENU 반환
        return representative.isEmpty() ? NO_MENU : representative;
    }

    private static boolean isRecommendedSafely(MenuDto menu) {
        if (menu == null) {
            return false;
        }
        try {
            return menu.recommended();
        } catch (Exception e) {
            log.warn("Failed to check recommended status for menu: {}", menu, e);
            return false;
        }
    }
}
package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.dto.MenuDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUtils {

    private static final int MAX_SIZE = 2;
    private static final String SEPARATOR = ", ";
    private static final String NO_MENU = "정보없음";

    // DTO용 오버로딩 메서드 추가
    public static String getMenusFromDto(List<MenuDto> menus) {
        if (menus == null || menus.isEmpty()) {
            return NO_MENU;
        }

        return menus.stream()
                .sorted(Comparator.comparingInt(MenuDto::menuIdx))
                .limit(MAX_SIZE)
                .map(MenuDto::name)
                .collect(Collectors.joining(SEPARATOR));
    }
}
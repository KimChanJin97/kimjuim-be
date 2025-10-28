package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.dto.MenuDto;

import java.text.DecimalFormat;
import java.util.List;

public class PriceUtils {

    private static final String NO_MENU_RECOMMENDED = "정보없음";
    private static final String PRICE_FORMAT = "%s원";
    private static final DecimalFormat COMMA_FORMATTER = new DecimalFormat("#,###");
    private static final String RANGE = "~";

    public static String formatPrice(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) {
            return NO_MENU_RECOMMENDED;
        }

        try {
            // 범위 가격 처리 (예: "8000~10000")
            if (priceStr.contains(RANGE)) {
                int priceInt = Integer.parseInt(priceStr.split(RANGE)[0].trim());
                return String.format(PRICE_FORMAT, COMMA_FORMATTER.format(priceInt));
            }

            // 일반 가격 처리 - 숫자로 파싱하여 콤마 추가
            // 쉼표나 "원" 등이 이미 포함되어 있을 수 있으므로 제거
            String cleanPrice = priceStr.replaceAll("[^0-9]", "");

            if (cleanPrice.isEmpty()) {
                return NO_MENU_RECOMMENDED;
            }

            int priceInt = Integer.parseInt(cleanPrice);
            return String.format(PRICE_FORMAT, COMMA_FORMATTER.format(priceInt));

        } catch (NumberFormatException e) {
            // 파싱 실패 시 원본 문자열에 "원"만 붙여서 반환
            return priceStr + "원";
        }
    }

    public static String getRecommendedPriceFromDto(List<MenuDto> menus) {
        if (menus == null || menus.isEmpty()) {
            return NO_MENU_RECOMMENDED;
        }

        MenuDto menu = menus.stream()
                .filter(MenuDto::isRecommended)
                .filter(m -> m.price() != null)
                .findFirst()
                .orElse(null);

        if (menu == null || menu.price() == null) {
            return NO_MENU_RECOMMENDED;
        }

        return formatPrice(menu.price());
    }
}
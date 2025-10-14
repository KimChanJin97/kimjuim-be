package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.domain.Menu;
import com.cjkim.kimjuim.restaurant.domain.Restaurant;
import java.text.DecimalFormat;

public class PriceUtils {

    private static final String NO_MENU_RECOMMENDED = "정보없음";
    private static final String PRICE_FORMAT = "%s원";
    private static final DecimalFormat COMMA_FORMATTER = new DecimalFormat("#,###");
    private static final String RANGE = "~";

    public static String getRecommendedPrice(Restaurant restaurant) {
        Menu menu = restaurant.getMenus().stream()
                .filter(m -> m.isRecommended() == true)
                .filter(m -> m.getPrice() != null)
                .findFirst()
                .orElse(null);

        if (menu == null || menu.getPrice() == null) {
            return NO_MENU_RECOMMENDED;
        }

        int price = Integer.parseInt(menu.getPrice().split("~")[0].trim());
        return String.format(PRICE_FORMAT, COMMA_FORMATTER.format(price));
    }

    public static String formatPrice(String priceStr) {
        if (priceStr == null) {
            return NO_MENU_RECOMMENDED;
        }
        if (priceStr.contains(RANGE)) {
            int priceInt = Integer.parseInt(priceStr.split("~")[0].trim());
            return String.format(PRICE_FORMAT, COMMA_FORMATTER.format(priceInt));
        }
        return String.format(PRICE_FORMAT, priceStr);
    }
}

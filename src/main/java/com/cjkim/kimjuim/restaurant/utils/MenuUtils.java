package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.domain.Menu;
import com.cjkim.kimjuim.restaurant.domain.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    private static final int MAX_SIZE = 2;
    private static final String SEPARATOR = ", ";
    private static final String NO_MENU = "정보없음";

    public static String getMenus(Restaurant restaurant) {
        List<Menu> menus = restaurant.getMenus();
        List<String> tmp = new ArrayList<>(10);

        // 메뉴가 아예 존재하지 않을 경우
        if (menus.size() < 1) {
            return NO_MENU;
        }

        for (Menu menu : menus) {
            if (tmp.size() < MAX_SIZE) {
                tmp.add(menu.getName());
            } else {
                break;
            }
        }
        return String.join(SEPARATOR, tmp);
    }
}


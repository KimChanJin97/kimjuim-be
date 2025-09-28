package com.cjkim.kimjuim.restaurant.utils;

public class DescriptionUtils {

    private static final String NO_DESCRIPTION = "정보없음";

    public static String getDescription(String description) {
        if (description == null) {
            return NO_DESCRIPTION;
        }
        return description;
    }
}

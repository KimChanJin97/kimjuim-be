package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.dto.BizHourDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BizHourUtils {

    private static final String EVERYDAY = "매일";
    private static final String EVERYDAY_BIZ_FORMAT = "매일 %s ~ %s";
    private static final String EVERYDAY_BIZ_WITH_BREAK_FORMAT = "매일 %s ~ %s (브레이크 %s ~ %s)";
    private static final String DAY_BIZ_FORMAT = "%s %s ~ %s";
    private static final String DAY_BIZ_WITH_BREAK_FORMAT = "%s %s ~ %s (브레이크 %s ~ %s)";
    private static final String NO_BIZ_HOUR = "정보없음";
    private static final Map<String, Integer> DAY_ORDER = Map.of(
            "월", 1,
            "화", 2,
            "수", 3,
            "목", 4,
            "금", 5,
            "토", 6,
            "일", 7
    );

    /**
     * 오늘의 영업시간 반환
     */
    public static String getTodayBizHourFromDto(List<BizHourDto> bizHourList, LocalDateTime now) {
        if (bizHourList == null || bizHourList.isEmpty()) {
            return NO_BIZ_HOUR;
        }

        // 매일
        BizHourDto everydayBiz = bizHourList.stream()
                .filter(b -> EVERYDAY.equals(b.day()))
                .findFirst()
                .orElse(null);

        BizHourDto everydayBreak = bizHourList.stream()
                .filter(b -> b.breakStart() != null)
                .findFirst()
                .orElse(null);

        // 영업시간과 브레이크타임이 모두 존재한다면
        if (everydayBiz != null && everydayBreak != null) {
            return String.format(EVERYDAY_BIZ_WITH_BREAK_FORMAT,
                    everydayBiz.bizStart(), everydayBiz.bizEnd(),
                    everydayBreak.breakStart(), everydayBreak.breakEnd());
        }

        // 영업시간만 존재한다면
        if (everydayBiz != null) {
            return String.format(EVERYDAY_BIZ_FORMAT,
                    everydayBiz.bizStart(), everydayBiz.bizEnd());
        }

        // 월, 화, 수, 목, 금, 토, 일
        BizHourDto dayBiz = bizHourList.stream()
                .filter(b -> b.day() != null && DAY_ORDER.get(b.day()) != null &&
                        DAY_ORDER.get(b.day()) == now.getDayOfWeek().getValue())
                .findFirst()
                .orElse(null);

        if (dayBiz != null) {
            String bizStart = dayBiz.bizStart();
            String bizEnd = dayBiz.bizEnd();
            String breakStart = dayBiz.breakStart();
            String breakEnd = dayBiz.breakEnd();

            // 영업시간과 브레이크타임이 모두 존재한다면
            if (bizStart != null && bizEnd != null && breakStart != null && breakEnd != null) {
                return String.format(DAY_BIZ_WITH_BREAK_FORMAT,
                        dayBiz.day(), bizStart, bizEnd, breakStart, breakEnd);
            }

            // 영업시간만 존재한다면
            if (bizStart != null && bizEnd != null) {
                return String.format(DAY_BIZ_FORMAT,
                        dayBiz.day(), bizStart, bizEnd);
            }
        }

        return NO_BIZ_HOUR;
    }
}
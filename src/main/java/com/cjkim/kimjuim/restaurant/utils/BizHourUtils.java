package com.cjkim.kimjuim.restaurant.utils;

import com.cjkim.kimjuim.restaurant.domain.BizHour;
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

    public static String getTodayBizHour(List<BizHour> bizHourList, LocalDateTime now) {
        // 매일
        BizHour everydayBiz = bizHourList.stream()
                .filter(b -> b.getDay().equals(EVERYDAY))
                .findFirst()
                .orElse(null);

        BizHour everydayBreak = bizHourList.stream()
                .filter(b -> b.getBreakStart() != null)
                .findFirst()
                .orElse(null);

        // 영업시간과 브레이크타임이 모두 존재한다면
        if (everydayBiz != null && everydayBreak != null) {
            String bizStart = everydayBiz.getBizStart();
            String bizEnd = everydayBiz.getBizEnd();
            String breakStart = everydayBreak.getBreakStart();
            String breakEnd = everydayBreak.getBizEnd();
            return String.format(EVERYDAY_BIZ_WITH_BREAK_FORMAT, bizStart, bizEnd, breakStart, breakEnd);
        }

        // 영업시간만 존재한다면
        if (everydayBiz != null) {
            String bizStart = everydayBiz.getBizStart();
            String bizEnd = everydayBiz.getBizEnd();
            return String.format(EVERYDAY_BIZ_FORMAT, bizStart, bizEnd);
        }

        // 월, 화, 수, 목, 금, 토, 일
        BizHour dayBiz = bizHourList.stream()
                .filter(b -> DAY_ORDER.get(b.getDay()) == now.getDayOfWeek().getValue())
                .findFirst()
                .orElse(null);

        if (dayBiz != null) {
            String day = dayBiz.getDay();
            String bizStart = dayBiz.getBizStart();
            String bizEnd = dayBiz.getBizEnd();
            String breakStart = dayBiz.getBreakStart();
            String breakEnd = dayBiz.getBreakEnd();

            // 영업시간과 브레이크타임이 모두 존재한다면
            if (bizStart != null && bizEnd != null && breakStart != null && breakEnd != null) {
                return String.format(DAY_BIZ_WITH_BREAK_FORMAT, day, bizStart, bizEnd, breakStart, breakEnd);
            }

            // 영업시간만 존재한다면
            if (bizStart != null && bizEnd != null) {
                return String.format(DAY_BIZ_FORMAT, day, bizStart, bizEnd);
            }
        }

        return NO_BIZ_HOUR;
    }
}

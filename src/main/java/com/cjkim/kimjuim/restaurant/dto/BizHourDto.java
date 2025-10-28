package com.cjkim.kimjuim.restaurant.dto;

public record BizHourDto(
        Long id,
        String day,
        String bizStart,
        String bizEnd,
        String breakStart,
        String breakEnd,
        String lastOrder
) {
    public static BizHourDto of(
            Long id,
            String day,
            String bizStart,
            String bizEnd,
            String breakStart,
            String breakEnd,
            String lastOrder
    ) {
        return new BizHourDto(id, day, bizStart, bizEnd, breakStart, breakEnd, lastOrder);
    }

    public static BizHourDto from(Object[] row, int startIndex) {
        return new BizHourDto(
                (Long) row[startIndex],           // biz_hour_id
                (String) row[startIndex + 1],     // day
                (String) row[startIndex + 2],     // biz_start
                (String) row[startIndex + 3],     // biz_end
                (String) row[startIndex + 4],     // break_start
                (String) row[startIndex + 5],     // break_end
                (String) row[startIndex + 6]      // last_order
        );
    }

    public static BizHourDto from(Object[] row) {
        return from(row, 16);
    }
}

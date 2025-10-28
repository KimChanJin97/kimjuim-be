package com.cjkim.kimjuim.restaurant.dto;

import java.util.Map;

public record BizHourDto(
        Long id,
        String day,
        String bizStart,
        String bizEnd,
        String breakStart,
        String breakEnd,
        String lastOrder
) {
    public static BizHourDto from(Map<String, Object> row) {
        return new BizHourDto(
                (Long) row.get("biz_hour_id"),
                (String) row.get("day"),
                (String) row.get("biz_start"),
                (String) row.get("biz_end"),
                (String) row.get("break_start"),
                (String) row.get("break_end"),
                (String) row.get("last_order")
        );
    }
}
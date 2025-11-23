//package com.cjkim.kimjuim.redis;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//@RequiredArgsConstructor
//public class RequestRateLimiterService {
//
//    private final StringRedisTemplate redisTemplate;
//
//    private static final int LIMIT = 15;                  // 임계치
//    private static final int WINDOW_SECONDS = 30;         // 요청 카운트 윈도우 (30초)
//
//    public boolean isBlacklisted(String ip) {
//        String blacklistIpKey = "blacklist:ip:" + ip;
//        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistIpKey));
//    }
//
//    public void recordRequest(String ip) {
//        String requestKey = "request:count:" + ip;
//        Long count = redisTemplate.opsForValue().increment(requestKey);
//
//        if (count != null && count == 1) {
//            // 첫 요청일 때만 TTL 설정
//            redisTemplate.expire(requestKey, Duration.ofSeconds(WINDOW_SECONDS));
//        }
//
//        if (count != null && count > LIMIT) {
//            // 블랙리스트 등록 (1일 TTL)
//            String blacklistIpKey = "blacklist:ip:" + ip;
//            redisTemplate.opsForValue().set(blacklistIpKey, "1", Duration.ofDays(1));
//        }
//    }
//}
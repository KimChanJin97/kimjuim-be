//package com.cjkim.kimjuim.auth.interceptor;
//
//import com.cjkim.kimjuim.redis.RequestRateLimiterException;
//import com.cjkim.kimjuim.redis.RequestRateLimiterExceptionInfo;
//import com.cjkim.kimjuim.redis.RequestRateLimiterService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@RequiredArgsConstructor
//@Component
//public class BlacklistInterceptor implements HandlerInterceptor {
//
//    private final RequestRateLimiterService requestRateLimiterService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String ip = extractClientIp(request);
//
//        // 요청 기록
//        requestRateLimiterService.recordRequest(ip);
//
//        // 블랙리스트 체크
//        boolean isBlackListed = requestRateLimiterService.isBlacklisted(ip);
//        if (isBlackListed) {
//            throw new RequestRateLimiterException(RequestRateLimiterExceptionInfo.BLACKLIST_ENROLLED);
//        }
//
//        return true;
//    }
//
//    private String extractClientIp(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip != null && !ip.isEmpty()) {
//            // 여러 개일 경우 첫 번째가 실제 클라이언트 IP
//            return ip.split(",")[0].trim();
//        }
//        return request.getRemoteAddr();
//    }
//}
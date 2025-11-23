//package com.cjkim.kimjuim.common.config;
//
//import static com.cjkim.kimjuim.auth.support.HttpMethod.*;
//import com.cjkim.kimjuim.auth.interceptor.BlacklistInterceptor;
//import com.cjkim.kimjuim.auth.interceptor.PathMatchInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class AuthConfig implements WebMvcConfigurer {
//
//    private final BlacklistInterceptor blacklistInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(BlacklistInterceptor());
//    }
//
//    private HandlerInterceptor BlacklistInterceptor() {
//        return new PathMatchInterceptor(blacklistInterceptor)
//                .excludePathPattern("/**", OPTIONS)
//                .includePathPattern("/api/v1/*", GET)
//                ;
//    }
//}

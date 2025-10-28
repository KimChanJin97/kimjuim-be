//package com.cjkim.kimjuim.auth.interceptor;
//
//import com.cjkim.kimjuim.auth.support.HttpMethod;
//import com.cjkim.kimjuim.auth.support.PathContainer;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//public class PathMatchInterceptor implements HandlerInterceptor {
//
//    private final HandlerInterceptor handlerInterceptor;
//    private final PathContainer pathContainer;
//
//    public PathMatchInterceptor(HandlerInterceptor handlerInterceptor) {
//        this.handlerInterceptor = handlerInterceptor;
//        this.pathContainer = new PathContainer();
//    }
//
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler
//    ) throws Exception {
//        String path = request.getServletPath();
//        String method = request.getMethod();
//
//        boolean matchesIncludePattern = pathContainer.matchesIncludePattern(path, method);
//        boolean matchesExcludePattern = pathContainer.matchesExcludePattern(path, method);
//
//        if (matchesIncludePattern) {
//            return handlerInterceptor.preHandle(request, response, handler);
//        }
//
//        if (!matchesIncludePattern || matchesExcludePattern) {
//            return true;
//        }
//
//        return handlerInterceptor.preHandle(request, response, handler);
//    }
//
//    public PathMatchInterceptor includePathPattern(String pathPattern, HttpMethod... httpMethod) {
//        pathContainer.addIncludePattern(pathPattern, httpMethod);
//        return this;
//    }
//
//    public PathMatchInterceptor excludePathPattern(String pathPattern, HttpMethod... httpMethod) {
//        pathContainer.addExcludePattern(pathPattern, httpMethod);
//        return this;
//    }
//}

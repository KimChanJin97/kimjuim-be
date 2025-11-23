package com.cjkim.kimjuim.auth.support;

import org.springframework.util.PathMatcher;

public record PathRequest(
        String path,
        HttpMethod method
) {
    public boolean matches(PathMatcher pathMatcher, String targetPath, String pathMethod) {
        return pathMatcher.match(path, targetPath) && method.matches(pathMethod);
    }
}

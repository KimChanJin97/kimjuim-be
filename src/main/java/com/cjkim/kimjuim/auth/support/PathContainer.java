package com.cjkim.kimjuim.auth.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathContainer {

    private final PathMatcher pathMatcher;
    private final List<PathRequest> includePatterns;
    private final List<PathRequest> excludePatterns;

    public PathContainer() {
        this.pathMatcher = new AntPathMatcher();
        this.includePatterns = new ArrayList<>();
        this.excludePatterns = new ArrayList<>();
    }

    public boolean matchesIncludePattern(String targetPath, String pathMethod) {
        return includePatterns.stream()
                .anyMatch(pathRequest -> pathRequest.matches(pathMatcher, targetPath, pathMethod));
    }

    public boolean matchesExcludePattern(String targetPath, String pathMethod) {
        return excludePatterns.stream()
                .anyMatch(pathPattern -> pathPattern.matches(pathMatcher, targetPath, pathMethod));
    }

    public void addIncludePattern(String path, HttpMethod... method) {
        for (HttpMethod httpMethod : method) {
            includePatterns.add(new PathRequest(path, httpMethod));
        }
    }

    public void addExcludePattern(String path, HttpMethod... method) {
        for (HttpMethod httpMethod : method) {
            excludePatterns.add(new PathRequest(path, httpMethod));
        }
    }
}

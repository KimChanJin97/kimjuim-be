package com.cjkim.kimjuim.question;

public record QuestionRequest(
        String name,
        String email,
        String type,
        String title,
        String content,
        boolean agreement
) {
}

package com.cjkim.kimjuim.question;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public void getSuggestions(
            @RequestPart("data") QuestionRequest request,
            @RequestPart(value = "file", required = false)MultipartFile file)
    {
        questionService.sendSimpleMailMessage(
                request.name(),
                request.email(),
                request.type(),
                request.title(),
                request.content(),
                file,
                request.agreement()
        );
    }
}

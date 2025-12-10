package com.server.application.usecase.tag.topic;

import java.time.LocalDateTime;
import java.util.List;

public record TopicResult(
        Long id,
        String title,
        String region,
        List<String> hashtags,
        String content,
        LocalDateTime createdAt
) {}

package com.server.application.usecase.tag.topic;

import java.util.List;

public record CreateTopicCommand(
        String title,
        String region,
        List<String> hashtags,
        String content
) {
}

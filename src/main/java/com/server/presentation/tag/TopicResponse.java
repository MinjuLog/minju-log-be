package com.server.presentation.tag;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "토픽 응답")
public record TopicResponse(
        @Schema(description = "토픽 아이디", example = "1")
        Long id,
        @Schema(description = "토픽 제목", example = "교육 정책")
        String title,
        @Schema(description = "지역", example = "서울")
        String region,
        @Schema(description = "해시태그 목록", example = "[\"교육\", \"정책\", \"서울\"]")
        List<String> hashtags,
        @Schema(description = "토픽 내용", example = "교육 관련 정책에 대한 상세 설명")
        String content,
        @Schema(description = "생성일시")
        LocalDateTime createdAt
) {}

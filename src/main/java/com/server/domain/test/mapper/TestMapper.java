package com.server.domain.test.mapper;

import com.server.domain.test.dto.request.TestRequestDto;
import com.server.domain.test.dto.response.TestResponseDto;
import com.server.domain.test.entity.TestEntity;

public class TestMapper {
    public static TestResponseDto toDto(TestEntity entity) {
        if (entity == null) return null;

        return TestResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }

    public static TestEntity toEntity(TestRequestDto dto) {
        if (dto == null) return null;

        return TestEntity.builder()
                .title(dto.title())
                .content(dto.content())
                .build();
    }
}

package com.server.domain.test.service;

import com.server.domain.test.dto.request.TestRequestDto;
import com.server.domain.test.dto.response.TestResponseDto;
import com.server.domain.test.entity.TestEntity;
import com.server.domain.test.mapper.TestMapper;
import com.server.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public List<TestResponseDto> getAllTests() {
        return testRepository.findAll().stream()
                .map(TestMapper::toDto)
                .collect(Collectors.toList());
    }

    public TestResponseDto getTest(Long id) {
        return testRepository.findById(id)
                .map(TestMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public TestResponseDto createTest(TestRequestDto dto) {
        TestEntity entity = TestMapper.toEntity(dto);
        return TestMapper.toDto(testRepository.save(entity));
    }

    @Transactional
    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }
}

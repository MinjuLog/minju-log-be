package com.server.domain.test.controller;

import com.server.domain.test.dto.request.TestRequestDto;
import com.server.domain.test.dto.response.TestResponseDto;
import com.server.domain.test.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Test", description = "Test API")
public class TestController {

    private final TestService testService;

    @Operation(summary = "전체 테스트 조회")
    @GetMapping
    public ResponseEntity<List<TestResponseDto>> getAllTests() {
        return ResponseEntity.ok(testService.getAllTests());
    }

    @Operation(summary = "단일 테스트 조회")
    @GetMapping("/{id}")
    public ResponseEntity<TestResponseDto> getTest(@PathVariable Long id) {
        return ResponseEntity.ok(testService.getTest(id));
    }

    @Operation(summary = "테스트 생성")
    @PostMapping
    public ResponseEntity<TestResponseDto> createTest(@RequestBody TestRequestDto dto) {
        return ResponseEntity.ok(testService.createTest(dto));
    }

    @Operation(summary = "테스트 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

}

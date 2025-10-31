package com.server.domain.test.controller;

import com.server.domain.test.dto.request.TestRequestDto;
import com.server.domain.test.dto.response.TestResponseDto;
import com.server.domain.test.service.TestService;
import com.server.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "Test", description = "Test API")
public class TestController {

    private final TestService testService;

    @Operation(summary = "전체 테스트 조회")
    @GetMapping
    public BaseResponse<List<TestResponseDto>> getAllTests() {
        return BaseResponse.onSuccess(testService.getAllTests());
    }

    @Operation(summary = "단일 테스트 조회")
    @GetMapping("/{id}")
    public BaseResponse<TestResponseDto> getTest(@PathVariable Long id) {
        return BaseResponse.onSuccess(testService.getTest(id));
    }

    @Operation(summary = "테스트 생성")
    @PostMapping
    public BaseResponse<TestResponseDto> createTest(@RequestBody TestRequestDto dto) {
        return BaseResponse.onSuccess(testService.createTest(dto));
    }

    @Operation(summary = "테스트 삭제")
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return BaseResponse.onSuccess("");
    }

    @Operation(summary = "시큐리티 테스트")
    @GetMapping("/secure")
    public BaseResponse<String> securityTest() {
        return BaseResponse.onSuccess("");
    }

}

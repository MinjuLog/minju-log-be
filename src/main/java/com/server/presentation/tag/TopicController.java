package com.server.presentation.tag;

import com.server.application.service.tag.TopicApplicationService;
import com.server.application.usecase.tag.topic.TopicResult;
import com.server.application.usecase.tag.topic.CreateTopicCommand;
import com.server.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@Tag(name = "Topic", description = "Topic APIs")
public class TopicController {

    private final TopicApplicationService topicApplicationService;

    @PostMapping
    @Operation(summary = "토픽 생성")
    public BaseResponse<TopicResponse> create(@RequestBody CreateTopicRequest request) {
        var command = new CreateTopicCommand(request.title(), request.region(), request.hashtags(), request.content());
        var result = topicApplicationService.create(command);
        return BaseResponse.onSuccess(new TopicResponse(result.id(), result.title(), result.region(), result.hashtags(), result.content(), result.createdAt()));
    }

    @GetMapping
    @Operation(summary = "토픽 목록 조회")
    public BaseResponse<List<TopicResponse>> list() {
        List<TopicResult> results = topicApplicationService.getList();
        List<TopicResponse> responses = results.stream()
                .map(r -> new TopicResponse(r.id(), r.title(), r.region(), r.hashtags(), r.content(), r.createdAt()))
                .toList();
        return BaseResponse.onSuccess(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "토픽 상세 조회")
    public BaseResponse<TopicResponse> detail(@PathVariable Long id) {
        TopicResult result = topicApplicationService.getDetail(id);
        return BaseResponse.onSuccess(new TopicResponse(result.id(), result.title(), result.region(), result.hashtags(), result.content(), result.createdAt()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "토픽 삭제")
    public BaseResponse<Boolean> delete(@PathVariable Long id) {
        topicApplicationService.delete(id);
        return BaseResponse.onSuccess(true);
    }
}



package com.server.application.usecase.tag.topic;

import com.server.domain.entity.tag.Topic;
import com.server.domain.repository.tag.TopicRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTopicDetailService implements GetTopicDetailUseCase {

    private final TopicRepository topicRepository;

    @Override
    public TopicResult execute(Long topicId) {
        return topicRepository.findById(topicId)
                .map(this::mapToResult)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    private TopicResult mapToResult(Topic topic) {
        var hashtagList = Arrays.stream(topic.getHashtags().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        
        return new TopicResult(
                topic.getId(),
                topic.getTitle(),
                topic.getRegion(),
                hashtagList,
                topic.getContent(),
                topic.getCreatedAt()
        );
    }
}

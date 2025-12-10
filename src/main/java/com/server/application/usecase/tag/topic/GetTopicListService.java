package com.server.application.usecase.tag.topic;

import com.server.domain.entity.tag.Topic;
import com.server.domain.repository.tag.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTopicListService implements GetTopicListUseCase {

    private final TopicRepository topicRepository;

    @Override
    public List<TopicResult> execute() {
        return topicRepository.findAll().stream()
                .map(this::mapToResult)
                .toList();
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

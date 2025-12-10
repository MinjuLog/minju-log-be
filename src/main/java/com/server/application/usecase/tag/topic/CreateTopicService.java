package com.server.application.usecase.tag.topic;

import com.server.domain.entity.tag.Topic;
import com.server.domain.repository.tag.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateTopicService implements CreateTopicUseCase {

    private final TopicRepository topicRepository;

    @Override
    public TopicResult execute(CreateTopicCommand command) {
        // List<String>을 콤마로 구분된 문자열로 변환
        String hashtagsString = String.join(",", command.hashtags());
        
        Topic topic = Topic.builder()
                .title(command.title())
                .region(command.region())
                .hashtags(hashtagsString)
                .content(command.content())
                .build();

        Topic savedTopic = topicRepository.save(topic);

        return mapToResult(savedTopic);
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

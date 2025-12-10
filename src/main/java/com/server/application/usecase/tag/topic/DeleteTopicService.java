package com.server.application.usecase.tag.topic;

import com.server.domain.repository.tag.TopicRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTopicService implements DeleteTopicUseCase {

    private final TopicRepository topicRepository;

    @Override
    public void execute(DeleteTopicCommand command) {
        topicRepository.deleteById(command.topicId());
    }
}

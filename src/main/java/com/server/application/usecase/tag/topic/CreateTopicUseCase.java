package com.server.application.usecase.tag.topic;

public interface CreateTopicUseCase {
    TopicResult execute(CreateTopicCommand command);
}

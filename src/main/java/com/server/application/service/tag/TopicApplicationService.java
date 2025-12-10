package com.server.application.service.tag;

import com.server.application.usecase.tag.topic.DeleteTopicCommand;
import com.server.application.usecase.tag.topic.DeleteTopicUseCase;
import com.server.application.usecase.tag.topic.GetTopicDetailUseCase;
import com.server.application.usecase.tag.topic.GetTopicListUseCase;
import com.server.application.usecase.tag.topic.TopicResult;
import com.server.application.usecase.tag.topic.CreateTopicCommand;
import com.server.application.usecase.tag.topic.CreateTopicUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicApplicationService {

    private final GetTopicListUseCase getTopicListUseCase;
    private final GetTopicDetailUseCase getTopicDetailUseCase;
    private final DeleteTopicUseCase deleteTopicUseCase;
    private final CreateTopicUseCase createTopicUseCase;

    @Transactional(readOnly = true)
    public List<TopicResult> getList() {
        return getTopicListUseCase.execute();
    }

    @Transactional(readOnly = true)
    public TopicResult getDetail(Long topicId) {
        return getTopicDetailUseCase.execute(topicId);
    }

    public void delete(Long topicId) {
        deleteTopicUseCase.execute(new DeleteTopicCommand(topicId));
    }

    public TopicResult create(CreateTopicCommand command) {
        return createTopicUseCase.execute(command);
    }
}

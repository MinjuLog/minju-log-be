package com.server.presentation.converter;

import com.server.application.usecase.proposal.proposal.CreateProposalCommand;
import com.server.presentation.proposal.proposal.CreateProposalRequest;

import java.time.LocalDate;
import java.util.List;

public class CreateProposalConverter {

    private CreateProposalConverter() {}

    public static CreateProposalCommand toCommand(CreateProposalRequest request) {
        List<String> hashtags = request.hashtags();
        LocalDate dueDate = null;
        if (request.dueDate() != null && !request.dueDate().isBlank()) {
            dueDate = LocalDate.parse(request.dueDate());
        }

        return new CreateProposalCommand(
                request.userId(),
                request.title(),
                request.body(),
                request.topicId(),
                hashtags,
                dueDate
        );
    }
}

package com.server.application.usecase.proposal.proposal;

import java.time.LocalDate;
import java.util.List;

public record CreateProposalCommand(
        Long userId,
        String title,
        String body,
        Long topicId,
        List<String> hashtags,
        LocalDate dueDate
) {}

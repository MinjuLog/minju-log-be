package com.server.application.usecase.proposal.proposal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateProposalResult(
        Long proposalId,
        String title,
        String body,
        String status,
        List<String> hashtags,
        LocalDate dueDate,
        LocalDateTime createdAt
) {}

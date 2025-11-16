package com.server.application.usecase.proposal.proposal;

import com.server.domain.entity.proposal.ProposalStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProposalListItemResult(
        Long id,
        String title,
        ProposalStatus status,
        Long viewCount,
        List<String> hashtags,
        LocalDate dueDate,
        Long agreeSignatureCount,
        Long disagreeSignatureCount,
        Long agreeVoteCount,
        Long disagreeVoteCount,
        LocalDateTime createdAt
) {}


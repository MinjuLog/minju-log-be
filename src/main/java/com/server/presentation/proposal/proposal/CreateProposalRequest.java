package com.server.presentation.proposal.proposal;

import java.util.List;

public record CreateProposalRequest(
        Long userId,
        String title,
        String body,
        List<String> hashtags,
        String dueDate // yyyy-MM-dd
) {}


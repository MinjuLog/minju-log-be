package com.server.application.usecase.proposal.proposal;

import com.server.application.usecase.proposal.signature.SignatureListItemResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record GetProposalDetailResult(
        Long id,
        String title,
        String body,
        String status,
        Long viewCount,
        List<String> hashtags,
        Long topicId,
        String topicName,
        LocalDate dueDate,
        long agreeSignatureCount,
        long disagreeSignatureCount,
        long agreeVoteCount,
        long disagreeVoteCount,
        MySignature mySignature,
        MyVote myVote,
        List<SignatureListItemResult> recentSignatures,
        LocalDateTime createdAt
) {
    public static record MySignature(boolean didSign, String type, String content) {}
    public static record MyVote(boolean didVote, String type) {}
}


package com.server.presentation.proposal.vote;

import com.server.domain.entity.proposal.ProposalVoteType;
import io.swagger.v3.oas.annotations.media.Schema;

public record VoteProposalRequest(
        @Schema(description = "사용자 아이디", example = "1")
        Long userId,

        @Schema(description = "투표 타입", example = "AGREE")
        ProposalVoteType voteType // "AGREE" or "DISAGREE"
) {}

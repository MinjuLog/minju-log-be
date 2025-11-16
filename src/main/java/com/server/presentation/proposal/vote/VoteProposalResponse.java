package com.server.presentation.proposal.vote;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "투표 응답")
public record VoteProposalResponse(
        @Schema(description = "투표 아이디", example = "21") Long voteId,
        @Schema(description = "투표 타입", example = "AGREE") String voteType
) {}
package com.server.application.usecase.proposal.proposal;

public interface GetProposalDetailUseCase {
    GetProposalDetailResult execute(Long proposalId, Long userId);
}


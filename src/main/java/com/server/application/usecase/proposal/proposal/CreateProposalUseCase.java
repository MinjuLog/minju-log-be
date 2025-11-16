package com.server.application.usecase.proposal.proposal;

public interface CreateProposalUseCase {
    CreateProposalResult execute(CreateProposalCommand command);
}

package com.server.application.usecase.proposal.signature;

public interface SignProposalUseCase {
    SignProposalResult execute(SignProposalCommand command);
}

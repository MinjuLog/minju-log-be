package com.server.presentation.converter;

import com.server.application.usecase.proposal.signature.SignProposalCommand;
import com.server.presentation.proposal.signature.SignProposalRequest;
import com.server.domain.entity.proposal.ProposalSignatureType;

public class SignProposalConverter {

    private SignProposalConverter() {}

    public static SignProposalCommand toCommand(Long proposalId, SignProposalRequest request) {
        return new SignProposalCommand(
                request.userId(),
                proposalId,
                request.signatureType(),
                request.content()
        );
    }
}

package com.server.application.usecase.proposal.signature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProposalSignaturesUseCase {
    Page<SignatureListItemResult> execute(Long proposalId, Pageable pageable);
}

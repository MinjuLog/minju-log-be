package com.server.application.usecase.proposal.proposal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProposalListUseCase {
    Page<ProposalListItemResult> execute(String keyword, String status, String hashtag, String sort, Pageable pageable);
}


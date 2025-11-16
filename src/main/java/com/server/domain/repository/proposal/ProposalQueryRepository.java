package com.server.domain.repository.proposal;

import com.server.application.usecase.proposal.proposal.ProposalListItemResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProposalQueryRepository {
    Page<ProposalListItemResult> search(String keyword, String sort, Pageable pageable);
}


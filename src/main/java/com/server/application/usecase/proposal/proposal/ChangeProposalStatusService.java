package com.server.application.usecase.proposal.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalStatus;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeProposalStatusService implements ChangeProposalStatusUseCase {

    private final ProposalRepository proposalRepository;

    @Override
    public void execute(ChangeProposalStatusCommand command) {
        Proposal p = proposalRepository.findById(command.proposalId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        p.changeStatus(ProposalStatus.valueOf(command.status()));
        proposalRepository.save(p);
    }
}

package com.server.application.usecase.proposal.signature;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.user.User;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.proposal.ProposalSignatureRepository;
import com.server.domain.repository.user.UserRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelSignatureProposalService implements CancelSignatureProposalUseCase {

    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalSignatureRepository signatureRepository;

    @Override
    public void execute(CancelSignatureProposalCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        Proposal proposal = proposalRepository.findById(command.proposalId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        signatureRepository.findByProposalAndUser(proposal, user)
                .ifPresent(signatureRepository::delete);
    }
}

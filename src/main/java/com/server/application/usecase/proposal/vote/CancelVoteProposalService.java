package com.server.application.usecase.proposal.vote;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.user.User;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.proposal.ProposalVoteRepository;
import com.server.domain.repository.user.UserRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelVoteProposalService implements CancelVoteProposalUseCase {

    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalVoteRepository voteRepository;

        @Override
        public void execute(CancelVoteProposalCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        Proposal proposal = proposalRepository.findById(command.proposalId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        voteRepository.findByProposalAndUser(proposal, user)
                .ifPresent(voteRepository::delete);
    }
}

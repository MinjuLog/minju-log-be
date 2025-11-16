package com.server.application.usecase.proposal.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalStatus;
import com.server.domain.entity.proposal.ProposalStatusHistory;
import com.server.domain.entity.user.User;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.user.UserRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProposalService implements CreateProposalUseCase {

    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;

        @Override
        public CreateProposalResult execute(CreateProposalCommand command) {

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        Proposal proposal = Proposal.builder()
                .user(user)
                .title(command.title())
                .body(command.body())
                .status(ProposalStatus.COLLECTING)
                .viewCount(0L)
                .hashtags(command.hashtags())
                .dueDate(command.dueDate())
                .build();

        Proposal saved = proposalRepository.save(proposal);

        return new CreateProposalResult(
                saved.getId(),
                saved.getTitle(),
                saved.getBody(),
                saved.getStatus().name(),
                saved.getHashtags(),
                saved.getDueDate(),
                saved.getCreatedAt()
        );
    }
}


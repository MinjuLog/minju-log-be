package com.server.application.usecase.proposal.vote;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalVote;
import com.server.domain.entity.proposal.ProposalVoteType;
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
public class VoteProposalService implements VoteProposalUseCase {

    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalVoteRepository voteRepository;

    @Override
    public VoteProposalResult execute(VoteProposalCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        Proposal proposal = proposalRepository.findById(command.proposalId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        ProposalVoteType requestedType = command.voteType();

        // 이미 투표했는지 체크
        var existingOpt = voteRepository.findByProposalAndUser(proposal, user);

        if (existingOpt.isPresent()) {
            ProposalVote existing = existingOpt.get();

            // 같은 타입으로 또 요청 -> 그냥 무시 or 에러, 여기선 에러 던지자
            if (existing.getVoteType() == requestedType) {
                throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
            }

            // 다른 타입으로 변경 (찬성 -> 반대, 반대 -> 찬성)
            existing = ProposalVote.builder()
                    .id(existing.getId())
                    .proposal(proposal)
                    .user(user)
                    .voteType(requestedType)
                    .build();

            ProposalVote saved = voteRepository.save(existing);
            return new VoteProposalResult(saved.getId(), proposal.getId(), user.getId(), saved.getVoteType().name());
        }

        // 처음 투표
        ProposalVote vote = ProposalVote.builder()
                .proposal(proposal)
                .user(user)
                .voteType(requestedType)
                .build();

    ProposalVote saved = voteRepository.save(vote);

    return new VoteProposalResult(saved.getId(), saved.getProposal().getId(), saved.getUser().getId(), saved.getVoteType().name());
    }
}

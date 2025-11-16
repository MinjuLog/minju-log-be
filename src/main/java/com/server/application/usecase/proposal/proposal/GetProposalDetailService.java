package com.server.application.usecase.proposal.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalSignature;
import com.server.domain.entity.proposal.ProposalSignatureType;
import com.server.domain.entity.proposal.ProposalVoteType;
import com.server.domain.entity.user.User;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.proposal.ProposalSignatureRepository;
import com.server.domain.repository.proposal.ProposalVoteRepository;
import com.server.domain.repository.user.UserRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import com.server.application.usecase.proposal.signature.SignatureListItemResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetProposalDetailService implements GetProposalDetailUseCase {

    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final ProposalVoteRepository voteRepository;
    private final ProposalSignatureRepository signatureRepository;

    @Override
    public GetProposalDetailResult execute(Long proposalId, Long userId) {

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        // 조회수 증가
        proposal.increaseViewCount();

        // 투표 개수
        long agreeVoteCount = voteRepository.countByProposalAndVoteType(proposal, ProposalVoteType.AGREE);
        long disagreeVoteCount = voteRepository.countByProposalAndVoteType(proposal, ProposalVoteType.DISAGREE);

        // 서명 개수
        long agreeSignatureCount = signatureRepository.countByProposalAndSignatureType(proposal, ProposalSignatureType.AGREE);
        long disagreeSignatureCount = signatureRepository.countByProposalAndSignatureType(proposal, ProposalSignatureType.DISAGREE);

        // 내가 한 투표
        var myVoteOpt = voteRepository.findByProposalAndUser(proposal, user);
        GetProposalDetailResult.MyVote myVote = myVoteOpt
                .map(v -> new GetProposalDetailResult.MyVote(true, v.getVoteType().name()))
                .orElse(new GetProposalDetailResult.MyVote(false, null));

        // 내가 한 서명
        var mySignatureOpt = signatureRepository.findByProposalAndUser(proposal, user);
        GetProposalDetailResult.MySignature mySignature = mySignatureOpt
                .map(s -> new GetProposalDetailResult.MySignature(true, s.getSignatureType().name(), s.getContent()))
                .orElse(new GetProposalDetailResult.MySignature(false, null, null));

        List<SignatureListItemResult> recent = signatureRepository.findAllByProposal(proposal).stream()
                .sorted(Comparator.comparing(ProposalSignature::getCreatedAt).reversed())
                .limit(10)
                .map(s -> new SignatureListItemResult(
                        s.getId(),
                        s.getUser().getId(),
                        s.getUser().getNickname(),
                        s.getSignatureType().name(),
                        s.getContent(),
                        s.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new GetProposalDetailResult(
                proposal.getId(),
                proposal.getTitle(),
                proposal.getBody(),
                proposal.getStatus().name(),
                proposal.getViewCount(),
                proposal.getHashtags(),
                proposal.getDueDate(),
                agreeSignatureCount,
                disagreeSignatureCount,
                agreeVoteCount,
                disagreeVoteCount,
                mySignature,
                myVote,
                recent,
                proposal.getCreatedAt()
        );
    }
}

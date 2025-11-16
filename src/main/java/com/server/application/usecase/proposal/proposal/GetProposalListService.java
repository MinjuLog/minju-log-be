package com.server.application.usecase.proposal.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalSignatureType;
import com.server.domain.repository.proposal.ProposalQueryRepository;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.proposal.ProposalSignatureRepository;
import com.server.domain.repository.proposal.ProposalVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetProposalListService implements GetProposalListUseCase {

    private final ProposalRepository proposalRepository;
    private final ProposalSignatureRepository signatureRepository;
    private final ProposalVoteRepository voteRepository;

    @Override
    public Page<ProposalListItemResult> execute(String keyword, String status, String hashtag, String sort, Pageable pageable) {

        Page<Proposal> page = proposalRepository.findAllByKeyword(keyword, pageable);

        List<ProposalListItemResult> list = page.getContent().stream()
                .filter(p -> {
                    if (status != null && !status.isBlank()) {
                        try {
                            return p.getStatus().name().equals(status);
                        } catch (Exception e) {
                            return false;
                        }
                    }
                    return true;
                })
                .filter(p -> {
                    if (hashtag != null && !hashtag.isBlank()) {
                        return p.getHashtags() != null && p.getHashtags().contains(hashtag);
                    }
                    return true;
                })
                .map(p -> {
                    Long agreeSig = signatureRepository.countByProposalAndSignatureType(p, ProposalSignatureType.AGREE);
                    Long disagreeSig = signatureRepository.countByProposalAndSignatureType(p, ProposalSignatureType.DISAGREE);
                    Long agreeVote = voteRepository.countByProposalAndVoteType(p, com.server.domain.entity.proposal.ProposalVoteType.AGREE);
                    Long disagreeVote = voteRepository.countByProposalAndVoteType(p, com.server.domain.entity.proposal.ProposalVoteType.DISAGREE);

                    return new ProposalListItemResult(
                            p.getId(),
                            p.getTitle(),
                            p.getStatus(),
                            p.getViewCount(),
                            p.getHashtags(),
                            p.getDueDate(),
                            agreeSig,
                            disagreeSig,
                            agreeVote,
                            disagreeVote,
                            p.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        // --- 정렬 옵션 적용 ---
        if ("agree-signature".equals(sort)) {
            list = list.stream()
                    .sorted((a, b) -> Long.compare(b.agreeSignatureCount(), a.agreeSignatureCount()))
                    .toList();
        } else if ("disagree-signature".equals(sort)) {
            list = list.stream()
                    .sorted((a, b) -> Long.compare(b.disagreeSignatureCount(), a.disagreeSignatureCount()))
                    .toList();
        } else if ("agree-vote".equals(sort)) {
            list = list.stream()
                    .sorted((a, b) -> Long.compare(b.agreeVoteCount(), a.agreeVoteCount()))
                    .toList();
        } else if ("disagree-vote".equals(sort)) {
            list = list.stream()
                    .sorted((a, b) -> Long.compare(b.disagreeVoteCount(), a.disagreeVoteCount()))
                    .toList();
        }

        return new PageImpl<>(list, pageable, page.getTotalElements());
    }
}


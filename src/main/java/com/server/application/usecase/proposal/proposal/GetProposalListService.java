package com.server.application.usecase.proposal.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalSignatureType;
import com.server.domain.entity.proposal.ProposalSortType;
import com.server.domain.entity.proposal.ProposalStatus;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.proposal.ProposalSignatureRepository;
import com.server.domain.repository.proposal.ProposalVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        // 1. Status enum 변환
        ProposalStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = ProposalStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                statusEnum = null;
            }
        }

        // 2. Sort type 변환
        ProposalSortType sortType = ProposalSortType.fromString(sort);

        // 3. DB 쿼리용 Pageable 생성
        Pageable dbPageable;
        if (sortType.isMemorySortRequired()) {
            // 메모리 정렬이 필요하면 모든 데이터 조회 (최신순으로 정렬)
            dbPageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            // DB 정렬인 경우 sort 적용
            dbPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortType.getDbSort());
        }

        // 4. DB에서 필터링된 데이터 조회
        Page<Proposal> page = proposalRepository.findAllByKeywordHashtagAndStatus(keyword, hashtag, statusEnum, dbPageable);

        // 5. 각 proposal의 투표/서명 개수 계산 및 DTO 변환
        List<ProposalListItemResult> list = page.getContent().stream()
                .map(p -> {
                    Long agreeSig = signatureRepository.countByProposalAndSignatureType(p, ProposalSignatureType.AGREE);
                    Long disagreeSig = signatureRepository.countByProposalAndSignatureType(p, ProposalSignatureType.DISAGREE);
                    Long agreeVote = voteRepository.countByProposalAndVoteType(p, com.server.domain.entity.proposal.ProposalVoteType.AGREE);
                    Long disagreeVote = voteRepository.countByProposalAndVoteType(p, com.server.domain.entity.proposal.ProposalVoteType.DISAGREE);

                    return new ProposalListItemResult(
                            p.getId(),
                            p.getTitle(),
                            p.getBody(),
                            p.getStatus(),
                            p.getViewCount(),
                            p.getHashtags(),
                            p.getTopic() != null ? p.getTopic().getId() : null,
                            p.getTopic() != null ? p.getTopic().getTitle() : null,
                            p.getDueDate(),
                            agreeSig,
                            disagreeSig,
                            agreeVote,
                            disagreeVote,
                            p.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        // 6. 메모리 기반 정렬이 필요하면 여기서 처리 후 페이지네이션 적용
        if (sortType.isMemorySortRequired()) {
            list = sortInMemory(list, sortType);
            
            // 페이지네이션 적용
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), list.size());
            List<ProposalListItemResult> content = start > list.size() ? List.of() : list.subList(start, end);
            
            return new PageImpl<>(content, pageable, list.size());
        }

        // DB 정렬인 경우: 올바른 Pageable로 반환
        return new PageImpl<>(list, dbPageable, page.getTotalElements());
    }

    /**
     * 메모리에서 정렬 수행
     */
    private List<ProposalListItemResult> sortInMemory(List<ProposalListItemResult> list, ProposalSortType sortType) {
        return switch (sortType) {
            case AGREE_SIGNATURE -> list.stream()
                    .sorted((a, b) -> Long.compare(b.agreeSignatureCount(), a.agreeSignatureCount()))
                    .collect(Collectors.toList());
            case DISAGREE_SIGNATURE -> list.stream()
                    .sorted((a, b) -> Long.compare(b.disagreeSignatureCount(), a.disagreeSignatureCount()))
                    .collect(Collectors.toList());
            case AGREE_VOTE -> list.stream()
                    .sorted((a, b) -> Long.compare(b.agreeVoteCount(), a.agreeVoteCount()))
                    .collect(Collectors.toList());
            case DISAGREE_VOTE -> list.stream()
                    .sorted((a, b) -> Long.compare(b.disagreeVoteCount(), a.disagreeVoteCount()))
                    .collect(Collectors.toList());
            case POPULAR -> list.stream()
                    .sorted((a, b) -> Long.compare(
                            (b.agreeSignatureCount() + b.disagreeSignatureCount() + b.agreeVoteCount() + b.disagreeVoteCount()),
                            (a.agreeSignatureCount() + a.disagreeSignatureCount() + a.agreeVoteCount() + a.disagreeVoteCount())
                    ))
                    .collect(Collectors.toList());
            default -> list;
        };
    }
}


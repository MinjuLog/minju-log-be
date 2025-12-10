package com.server.application.usecase.proposal.signature;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalSignature;
import com.server.domain.entity.proposal.ProposalSignatureType;
import com.server.domain.repository.proposal.ProposalRepository;
import com.server.domain.repository.proposal.ProposalSignatureRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetProposalSignaturesService implements GetProposalSignaturesUseCase {

    private final ProposalRepository proposalRepository;
    private final ProposalSignatureRepository signatureRepository;

    @Override
    public Page<SignatureListItemResult> execute(Long proposalId, Pageable pageable, String filter) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        List<ProposalSignature> all = signatureRepository.findAllByProposal(proposal);

        // filter 적용 (ALL, AGREE, DISAGREE)
        List<ProposalSignature> filtered = filterSignatures(all, filter);

        List<SignatureListItemResult> list = filtered.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(ProposalSignature::getCreatedAt)).reversed())
                .map(s -> new SignatureListItemResult(
                        s.getId(),
                        s.getUser().getId(),
                        s.getNickname(),
                        s.getSignatureType().name(),
                        s.getContent(),
                        s.getCreatedAt()
                ))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        List<SignatureListItemResult> content = start > end ? List.of() : list.subList(start, end);

        return new PageImpl<>(content, pageable, list.size());
    }

    private List<ProposalSignature> filterSignatures(List<ProposalSignature> signatures, String filter) {
        if (filter == null || filter.isBlank() || filter.equalsIgnoreCase("ALL")) {
            return signatures;
        }

        try {
            ProposalSignatureType type = ProposalSignatureType.valueOf(filter.toUpperCase());
            return signatures.stream()
                    .filter(s -> s.getSignatureType() == type)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return signatures;
        }
    }
}

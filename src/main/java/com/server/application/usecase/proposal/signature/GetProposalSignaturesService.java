package com.server.application.usecase.proposal.signature;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalSignature;
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
    public Page<SignatureListItemResult> execute(Long proposalId, Pageable pageable) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        List<ProposalSignature> all = signatureRepository.findAllByProposal(proposal);

        List<SignatureListItemResult> list = all.stream()
                .sorted(Comparator.comparing(ProposalSignature::getCreatedAt).reversed())
                .map(s -> new SignatureListItemResult(
                        s.getId(),
                        s.getUser().getId(),
                        s.getUser().getNickname(),
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
}

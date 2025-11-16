package com.server.application.usecase.proposal.signature;

import com.server.domain.entity.proposal.*;
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
public class SignProposalService implements SignProposalUseCase {

    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalSignatureRepository signatureRepository;

        @Override
        public SignProposalResult execute(SignProposalCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        Proposal proposal = proposalRepository.findById(command.proposalId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        // 이미 서명했으면 에러
        signatureRepository.findByProposalAndUser(proposal, user)
                .ifPresent(s -> {
                    throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
                });

        ProposalSignature signature = ProposalSignature.builder()
                .proposal(proposal)
                .user(user)
                .signatureType(command.signatureType())
                .content(command.content())
                .build();

                ProposalSignature saved = signatureRepository.save(signature);

                return new SignProposalResult(saved.getId(), saved.getSignatureType().name(), saved.getContent(), saved.getCreatedAt());
    }
}
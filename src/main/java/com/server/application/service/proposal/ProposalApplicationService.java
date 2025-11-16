package com.server.application.service.proposal;

import com.server.application.usecase.proposal.proposal.CreateProposalCommand;
import com.server.application.usecase.proposal.proposal.CreateProposalResult;
import com.server.application.usecase.proposal.proposal.CreateProposalUseCase;
import com.server.application.usecase.proposal.proposal.GetProposalDetailResult;
import com.server.application.usecase.proposal.proposal.GetProposalDetailUseCase;
import com.server.application.usecase.proposal.proposal.GetProposalListUseCase;
import com.server.application.usecase.proposal.proposal.ProposalListItemResult;
import com.server.application.usecase.proposal.vote.CancelVoteProposalCommand;
import com.server.application.usecase.proposal.vote.CancelVoteProposalUseCase;
import com.server.application.usecase.proposal.vote.VoteProposalCommand;
import com.server.application.usecase.proposal.vote.VoteProposalResult;
import com.server.application.usecase.proposal.vote.VoteProposalUseCase;
import com.server.application.usecase.proposal.signature.SignProposalCommand;
import com.server.application.usecase.proposal.signature.SignProposalResult;
import com.server.application.usecase.proposal.signature.SignProposalUseCase;
import com.server.application.usecase.proposal.signature.CancelSignatureProposalCommand;
import com.server.application.usecase.proposal.signature.CancelSignatureProposalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.server.application.usecase.proposal.signature.GetProposalSignaturesUseCase;
import com.server.application.usecase.proposal.signature.SignatureListItemResult;
import com.server.application.usecase.proposal.proposal.ChangeProposalStatusCommand;
import com.server.application.usecase.proposal.proposal.ChangeProposalStatusUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProposalApplicationService {

    private final CreateProposalUseCase createProposalUseCase;
    private final GetProposalDetailUseCase getProposalDetailUseCase;
    private final GetProposalListUseCase getProposalListUseCase;
    private final VoteProposalUseCase voteProposalUseCase;
    private final CancelVoteProposalUseCase cancelVoteProposalUseCase;
    private final SignProposalUseCase signProposalUseCase;
    private final CancelSignatureProposalUseCase cancelSignatureProposalUseCase;
    private final GetProposalSignaturesUseCase getProposalSignaturesUseCase;
    private final ChangeProposalStatusUseCase changeProposalStatusUseCase;

    public CreateProposalResult create(CreateProposalCommand command) {
        return createProposalUseCase.execute(command);
    }

    public VoteProposalResult vote(VoteProposalCommand command) {
        return voteProposalUseCase.execute(command);
    }

    public void cancelVote(CancelVoteProposalCommand command) {
        cancelVoteProposalUseCase.execute(command);
    }

    public SignProposalResult sign(SignProposalCommand command) {
        return signProposalUseCase.execute(command);
    }

    public void cancelSignature(CancelSignatureProposalCommand command) {
        cancelSignatureProposalUseCase.execute(command);
    }

    @Transactional(readOnly = true)
    public GetProposalDetailResult getDetail(Long proposalId, Long userId) {
        return getProposalDetailUseCase.execute(proposalId, userId);
    }

    @Transactional(readOnly = true)
    public Page<ProposalListItemResult> list(String keyword, String status, String hashtag, String sort, Pageable pageable) {
        return getProposalListUseCase.execute(keyword, status, hashtag, sort, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SignatureListItemResult> getSignatures(Long proposalId, Pageable pageable) {
        return getProposalSignaturesUseCase.execute(proposalId, pageable);
    }

    public void changeStatus(ChangeProposalStatusCommand command) {
        changeProposalStatusUseCase.execute(command);
    }
}

package com.server.presentation.proposal.vote;

import com.server.application.service.proposal.ProposalApplicationService;
import com.server.application.usecase.proposal.vote.*;
import com.server.presentation.converter.VoteProposalConverter;
import com.server.domain.entity.proposal.ProposalVoteType;
import com.server.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proposals")
@RequiredArgsConstructor
@Tag(name = "Vote", description = "Vote APIs")
public class ProposalVoteController {

        private final ProposalApplicationService proposalApplicationService;

    // 2. 투표하기
    @PostMapping("/{proposalId}/votes")
    @Operation(summary = "투표하기")
    public BaseResponse<VoteProposalResponse> vote(
            @PathVariable Long proposalId,
            @RequestBody VoteProposalRequest request
    ) {
        VoteProposalCommand command = VoteProposalConverter.toCommand(proposalId, request);

        VoteProposalResult result = proposalApplicationService.vote(command);
        return BaseResponse.onSuccess(new VoteProposalResponse(
                result.voteId(),
                result.voteType()
        ));
    }

    // 3. 투표 취소
    @DeleteMapping("/{proposalId}/votes")
    @Operation(summary = "투표 취소")
    public BaseResponse<Boolean> cancelVote(
            @PathVariable Long proposalId,
            @RequestParam Long userId
    ) {
        proposalApplicationService.cancelVote(new CancelVoteProposalCommand(userId, proposalId));
        return BaseResponse.onSuccess(Boolean.TRUE);
    }
}

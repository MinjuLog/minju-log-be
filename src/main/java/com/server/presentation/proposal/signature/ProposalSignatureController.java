package com.server.presentation.proposal.signature;

import com.server.application.service.proposal.ProposalApplicationService;
import com.server.application.usecase.proposal.signature.SignProposalCommand;
import com.server.application.usecase.proposal.signature.SignProposalResult;
import com.server.presentation.converter.SignProposalConverter;
import com.server.domain.entity.proposal.ProposalSignatureType;
import com.server.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proposals")
@RequiredArgsConstructor
@Tag(name = "Signature", description = "Signature APIs")
public class ProposalSignatureController {

        private final ProposalApplicationService proposalApplicationService;

    @PostMapping("/{proposalId}/signatures")
    @Operation(summary = "서명 등록")
    public BaseResponse<SignProposalResponse> sign(
            @PathVariable Long proposalId,
            @RequestBody SignProposalRequest request
    ) {
        SignProposalCommand command = SignProposalConverter.toCommand(proposalId, request);

        SignProposalResult result = proposalApplicationService.sign(command);
        return BaseResponse.onSuccess(new SignProposalResponse(
                result.signatureId(),
                result.signatureType(),
                result.content(),
                result.createdAt()
        ));
    }

            @DeleteMapping("/{proposalId}/signatures")
            @Operation(summary = "서명 취소")
            public BaseResponse<Boolean> cancelSignature(
                        @PathVariable Long proposalId,
                        @RequestParam Long userId
        ) {
                proposalApplicationService.cancelSignature(new com.server.application.usecase.proposal.signature.CancelSignatureProposalCommand(userId, proposalId));
                return BaseResponse.onSuccess(Boolean.TRUE);
        }

                    @GetMapping("/{proposalId}/signatures")
                    @Operation(summary = "서명 목록 조회")
                    public BaseResponse<org.springframework.data.domain.Page<com.server.presentation.proposal.signature.SignatureListItemResponse>> list(
                                @PathVariable Long proposalId,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size
                ) {
                        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
                        var pageResult = proposalApplicationService.getSignatures(proposalId, pageable);
                        var responsePage = pageResult.map(r -> new com.server.presentation.proposal.signature.SignatureListItemResponse(
                                        r.signatureId(), r.userId(), r.nickname(), r.signatureType(), r.content(), r.createdAt()
                        ));
                        return BaseResponse.onSuccess(responsePage);
                }
}


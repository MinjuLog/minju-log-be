package com.server.presentation.proposal.proposal;

import com.server.application.service.proposal.ProposalApplicationService;
import com.server.application.usecase.proposal.proposal.*;
import com.server.presentation.converter.CreateProposalConverter;
import com.server.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposals")
@Tag(name = "Proposal", description = "Proposal APIs")
public class ProposalController {

        private final ProposalApplicationService proposalApplicationService;

    @PostMapping
    @Operation(summary = "제안 생성")
    public BaseResponse<CreateProposalResponse> create(
            @RequestBody CreateProposalRequest request
    ) {
        var command = CreateProposalConverter.toCommand(request);
        var result = proposalApplicationService.create(command);

        return BaseResponse.onSuccess(
                new CreateProposalResponse(
                        result.proposalId(),
                        result.title(),
                        result.body(),
                        result.status(),
                        result.hashtags(),
                        result.dueDate(),
                        result.createdAt()
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "제안 상세 조회")
    public BaseResponse<GetProposalDetailResponse> findById(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        var result = proposalApplicationService.getDetail(id, userId);

        return BaseResponse.onSuccess(
                new GetProposalDetailResponse(result)
        );
    }

    @GetMapping
    @Operation(summary = "제안 목록 조회")
    public BaseResponse<Page<ProposalListItemResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String hashtag,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 커스텀 sort 파라미터만 사용, Pageable의 sort는 무시
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        var pageResult = proposalApplicationService.list(keyword, status, hashtag, sort, pageable);
        var responsePage = pageResult.map(ProposalListItemResponse::new);
        return BaseResponse.onSuccess(responsePage);
    }

        @GetMapping("/by-hashtag")
                @Operation(summary = "해시태그 기반 제안 조회")
        public BaseResponse<Page<ProposalListItemResponse>> byHashtag(
                        @RequestParam String name,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size
        ) {
                Pageable pageable = Pageable.ofSize(size).withPage(page);
                var pageResult = proposalApplicationService.list(null, null, name, "latest", pageable);
                var responsePage = pageResult.map(ProposalListItemResponse::new);
                return BaseResponse.onSuccess(responsePage);
        }

                @PatchMapping("/{id}/status")
                    @Operation(summary = "제안 상태 변경 (관리자)")
                public BaseResponse<Boolean> changeStatus(
                                @PathVariable Long id,
                                @RequestBody ChangeStatusRequest request
                ) {
                        proposalApplicationService.changeStatus(new com.server.application.usecase.proposal.proposal.ChangeProposalStatusCommand(id, request.status()));
                        return BaseResponse.onSuccess(Boolean.TRUE);
                }



}


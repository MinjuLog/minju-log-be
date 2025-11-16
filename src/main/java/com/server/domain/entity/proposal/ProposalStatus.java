package com.server.domain.entity.proposal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProposalStatus {

    COLLECTING("의견 취합중"),
    DELIVERED("의견 전달 완료"),
    REPORTING("보도중"),
    COMPLETED("반영 완료");

    private final String description;
}
package com.server.domain.entity.proposal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProposalStatus {
    WAITING,
    IN_PROGRESS,
    COMPLETED,
    REJECTED
}

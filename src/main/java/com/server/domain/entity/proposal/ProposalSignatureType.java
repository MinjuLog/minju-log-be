package com.server.domain.entity.proposal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProposalSignatureType {
    AGREE,      // 찬성
    DISAGREE    // 반대
}

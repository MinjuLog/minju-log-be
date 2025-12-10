package com.server.domain.entity.proposal;

import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * 제안 목록 정렬 방식
 */
@Getter
public enum ProposalSortType {
    LATEST("최신순", Sort.by(Sort.Direction.DESC, "createdAt"), false),
    POPULAR("인기순", null, true),
    VIEWS("조회수순", Sort.by(Sort.Direction.DESC, "viewCount"), false),
    AGREE_SIGNATURE("찬성 서명순", null, true),
    DISAGREE_SIGNATURE("반대 서명순", null, true),
    AGREE_VOTE("찬성 투표순", null, true),
    DISAGREE_VOTE("반대 투표순", null, true);

    private final String description;
    private final Sort dbSort;
    private final boolean isMemorySortRequired;

    ProposalSortType(String description, Sort dbSort, boolean isMemorySortRequired) {
        this.description = description;
        this.dbSort = dbSort;
        this.isMemorySortRequired = isMemorySortRequired;
    }

    /**
     * 문자열로부터 ProposalSortType 변환
     * 유효하지 않은 값이면 기본값(LATEST) 반환
     */
    public static ProposalSortType fromString(String value) {
        if (value == null || value.isBlank()) {
            return LATEST;
        }
        try {
            return ProposalSortType.valueOf(value.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return LATEST;
        }
    }
}

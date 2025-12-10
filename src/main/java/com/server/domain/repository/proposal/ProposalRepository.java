package com.server.domain.repository.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    @Query("""
        SELECT p
        FROM Proposal p
        WHERE (:keyword IS NULL 
           OR p.title LIKE %:keyword% 
           OR p.body LIKE %:keyword%
           OR EXISTS (SELECT 1 FROM p.hashtags h WHERE h LIKE %:keyword% OR h = :keyword OR h = CONCAT('#', :keyword)))
           AND (:hashtag IS NULL 
           OR EXISTS (SELECT 1 FROM p.hashtags h WHERE h = :hashtag OR h = CONCAT('#', :hashtag)))
        """)
    Page<Proposal> findAllByKeywordAndHashtag(
            @Param("keyword") String keyword,
            @Param("hashtag") String hashtag,
            Pageable pageable
    );

    /**
     * keyword, hashtag, status로 필터링한 제안 목록 조회
     * status가 NULL이면 모든 상태의 제안 포함
     * keyword는 제목, 본문, 해시태그 모두에서 검색 (부분 매칭 포함)
     * hashtag는 정확한 매칭 (#있는 그대로 또는 #없이)
     */
    @Query("""
        SELECT p
        FROM Proposal p
        WHERE (:keyword IS NULL 
           OR p.title LIKE %:keyword% 
           OR p.body LIKE %:keyword%
           OR EXISTS (SELECT 1 FROM p.hashtags h WHERE h LIKE %:keyword% OR h = :keyword OR h = CONCAT('#', :keyword)))
           AND (:hashtag IS NULL 
           OR EXISTS (SELECT 1 FROM p.hashtags h WHERE h = :hashtag OR h = CONCAT('#', :hashtag)))
           AND (:status IS NULL 
           OR p.status = :status)
        """)
    Page<Proposal> findAllByKeywordHashtagAndStatus(
            @Param("keyword") String keyword,
            @Param("hashtag") String hashtag,
            @Param("status") ProposalStatus status,
            Pageable pageable
    );
}

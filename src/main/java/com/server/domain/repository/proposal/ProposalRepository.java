package com.server.domain.repository.proposal;

import com.server.domain.entity.proposal.Proposal;
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
           OR p.body LIKE %:keyword%)
        """)
    Page<Proposal> findAllByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}

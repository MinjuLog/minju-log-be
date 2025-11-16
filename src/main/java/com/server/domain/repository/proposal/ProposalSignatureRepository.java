package com.server.domain.repository.proposal;


import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalSignature;
import com.server.domain.entity.proposal.ProposalSignatureType;
import com.server.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ProposalSignatureRepository extends JpaRepository<ProposalSignature, Long> {

    Optional<ProposalSignature> findByProposalAndUser(Proposal proposal, User user);

    long countByProposalAndSignatureType(Proposal proposal, ProposalSignatureType type);

    List<ProposalSignature> findAllByProposal(Proposal proposal);
}

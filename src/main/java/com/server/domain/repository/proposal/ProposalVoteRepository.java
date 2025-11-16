package com.server.domain.repository.proposal;

import com.server.domain.entity.proposal.Proposal;
import com.server.domain.entity.proposal.ProposalVote;
import com.server.domain.entity.proposal.ProposalVoteType;
import com.server.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ProposalVoteRepository extends JpaRepository<ProposalVote, Long> {

    Optional<ProposalVote> findByProposalAndUser(Proposal proposal, User user);

    long countByProposalAndVoteType(Proposal proposal, ProposalVoteType voteType);

    List<ProposalVote> findAllByProposal(Proposal proposal);
}

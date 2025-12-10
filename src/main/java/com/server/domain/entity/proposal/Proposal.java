package com.server.domain.entity.proposal;

import com.server.domain.entity.user.User;
import com.server.domain.entity.tag.Topic;
import com.server.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proposal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Proposal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProposalStatus status;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false, name = "view_count")
    private Long viewCount;

    @ElementCollection
    @CollectionTable(name = "proposal_hashtag", joinColumns = @JoinColumn(name = "proposal_id"))
    private List<String> hashtags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private LocalDate dueDate;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private List<ProposalVote> votes;

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    public void changeStatus(ProposalStatus status) {
        this.status = status;
    }

}


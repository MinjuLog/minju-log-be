package com.server.domain.entity.user;

import com.server.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blocked_ip", indexes = {
        @Index(name = "idx_ip_address", columnList = "ip_address"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BlockedIp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ip_address", nullable = false, unique = true, length = 45)
    private String ipAddress;

    @Column(name = "reason", length = 500)
    private String reason;

    public static BlockedIp of(User user, String ipAddress, String reason) {
        return BlockedIp.builder()
                .user(user)
                .ipAddress(ipAddress)
                .reason(reason)
                .build();
    }
}

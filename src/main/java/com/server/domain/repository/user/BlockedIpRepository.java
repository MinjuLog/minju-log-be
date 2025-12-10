package com.server.domain.repository.user;

import com.server.domain.entity.user.BlockedIp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedIpRepository extends JpaRepository<BlockedIp, Long> {
    Optional<BlockedIp> findByIpAddress(String ipAddress);
    boolean existsByIpAddress(String ipAddress);
}

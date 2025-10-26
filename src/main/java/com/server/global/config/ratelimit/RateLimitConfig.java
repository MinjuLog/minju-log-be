package com.server.global.config.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
//@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
@RequiredArgsConstructor
public class RateLimitConfig {

    private final RedisClient lettuceRedisClient;

    // Redis(StringRedisTemplate)로 ProxyManager를 생성합니다.
    // 시스템 기준으로 동일한 버킷 상태를 공유합니다.
    @Bean
    public LettuceBasedProxyManager proxyManager() {
        // Lettuce는 stateful connection을 생성해야 함
        StatefulRedisConnection<String, byte[]> connection =
                lettuceRedisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        // 버킷 상태의 만료 시간 설정 (10분)
        var expiration = ExpirationAfterWriteStrategy
                .basedOnTimeForRefillingBucketUpToMax(java.time.Duration.ofMinutes(10));

        return LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(expiration)
                .build();
    }

    //기본 버킷 정책: period 마다 limit 만큼 토큰 재충전 (1초 10회 등)
    @Bean
    public BucketConfiguration defaultBucketConfiguration(RateLimitProperties props) {
        int limit = props.getLimit();
        Duration period = props.getPeriod();

        // limit 개수의 토큰을 period 마다 재충전
        Bandwidth bandwidth = Bandwidth.builder().capacity(limit).refillIntervally(limit, period)
                .build();

        return BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();
    }

}

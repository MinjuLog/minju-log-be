package com.server.domain.entity.tag;

import com.server.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String hashtags;  // 콤마로 구분된 해시태그 문자열

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}


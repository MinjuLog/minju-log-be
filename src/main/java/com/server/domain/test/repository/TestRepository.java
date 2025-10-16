package com.server.domain.test.repository;

import com.server.domain.test.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TestRepository extends JpaRepository<TestEntity, Long> { }

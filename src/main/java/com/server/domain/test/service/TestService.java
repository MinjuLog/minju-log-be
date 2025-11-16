package com.server.domain.test.service;

/**
 * Domain layer must not depend on framework concerns (Spring, transactions).
 * This class is intentionally kept framework-agnostic. Application-level
 * transactional behavior is provided by {@code com.server.application.service.test.TestApplicationService}.
 */
public final class TestService {
    private TestService() {}
}

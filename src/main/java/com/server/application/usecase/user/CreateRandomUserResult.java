package com.server.application.usecase.user;

import java.time.LocalDateTime;

public record CreateRandomUserResult(Long userId, String nickname, LocalDateTime createdAt) {}

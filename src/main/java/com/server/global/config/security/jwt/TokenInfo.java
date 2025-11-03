package com.server.global.config.security.jwt;

// AccessToken + RefreshToken DTO
public record TokenInfo(
    String accessToken,
    String refreshToken
) {
}

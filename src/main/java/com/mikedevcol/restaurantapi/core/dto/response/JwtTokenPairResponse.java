package com.mikedevcol.restaurantapi.core.dto.response;

import java.time.Instant;

public record JwtTokenPairResponse(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt) {
}
package com.mikedevcol.restaurantapi.dto.response;

import java.time.Instant;

public record JwtTokenPairResponse(
    String accessToken,
    Instant accessTokenExpiresAt,
    String refreshToken,
    Instant refreshTokenExpiresAt) {
}
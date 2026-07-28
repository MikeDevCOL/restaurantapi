package com.mikedevcol.restaurantapi.security;

import java.time.Instant;

public record JwtTokenClaims(
    String subject,
    String issuer,
    String tokenType,
    Instant issuedAt,
    Instant expiresAt) {
}
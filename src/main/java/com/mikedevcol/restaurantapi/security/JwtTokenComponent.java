package com.mikedevcol.restaurantapi.security;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.mikedevcol.restaurantapi.config.JwtProperties;
import com.mikedevcol.restaurantapi.dto.response.JwtTokenPairResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenComponent {

  private static final String TOKEN_TYPE_CLAIM = "token_type";
  private static final String ACCESS_TOKEN_TYPE = "access";
  private static final String REFRESH_TOKEN_TYPE = "refresh";
  private static final int MIN_SECRET_LENGTH = 32;

  private final JwtProperties jwtProperties;
  private final Clock clock = Clock.systemUTC();

  public JwtTokenPairResponse createTokens(String subject) {
    String normalizedSubject = validateSubject(subject);
    Instant now = Instant.now(clock);
    Instant accessTokenExpiresAt = now.plusSeconds(jwtProperties.getAccessTokenExpirationSeconds());
    Instant refreshTokenExpiresAt = now.plusSeconds(jwtProperties.getRefreshTokenExpirationSeconds());

    String accessToken = JWT.create()
        .withIssuer(jwtProperties.getIssuer())
        .withSubject(normalizedSubject)
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(accessTokenExpiresAt))
        .withJWTId(UUID.randomUUID().toString())
        .withClaim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
        .sign(algorithm());

    String refreshToken = JWT.create()
        .withIssuer(jwtProperties.getIssuer())
        .withSubject(normalizedSubject)
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(refreshTokenExpiresAt))
        .withJWTId(UUID.randomUUID().toString())
        .withClaim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
        .sign(algorithm());

    return new JwtTokenPairResponse(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
  }

  public JwtTokenClaims readToken(String token) {
    DecodedJWT decodedJWT = verifier().verify(validateTokenInput(token));
    return toClaims(decodedJWT);
  }

  public boolean validateToken(String token) {
    try {
      verifier().verify(validateTokenInput(token));
      return true;
    } catch (JWTVerificationException | IllegalArgumentException ex) {
      return false;
    }
  }

  public JwtTokenPairResponse rotateTokens(String accessToken) {
    JwtTokenClaims claims = readToken(accessToken);
    if (!ACCESS_TOKEN_TYPE.equals(claims.tokenType())) {
      throw new IllegalArgumentException("Token rotation requires an access token");
    }
    return createTokens(claims.subject());
  }

  private JwtTokenClaims toClaims(DecodedJWT decodedJWT) {
    Instant issuedAt = decodedJWT.getIssuedAt() != null ? decodedJWT.getIssuedAt().toInstant() : null;
    Instant expiresAt = decodedJWT.getExpiresAt() != null ? decodedJWT.getExpiresAt().toInstant() : null;
    String tokenType = decodedJWT.getClaim(TOKEN_TYPE_CLAIM).isNull()
        ? null
        : decodedJWT.getClaim(TOKEN_TYPE_CLAIM).asString();

    return new JwtTokenClaims(decodedJWT.getSubject(), decodedJWT.getIssuer(), tokenType, issuedAt, expiresAt);
  }

  private String validateSubject(String subject) {
    if (subject == null || subject.isBlank()) {
      throw new IllegalArgumentException("Subject is required to create tokens");
    }
    return subject.trim();
  }

  private String validateTokenInput(String token) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Token is required");
    }
    return token.trim();
  }

  private Algorithm algorithm() {
    String secret = Objects.requireNonNull(jwtProperties.getSecret(), "JWT secret is required").trim();
    if (secret.length() < MIN_SECRET_LENGTH) {
      throw new IllegalStateException("JWT secret must be at least 32 characters long");
    }
    return Algorithm.HMAC256(secret);
  }

  private JWTVerifier verifier() {
    String issuer = Objects.requireNonNull(jwtProperties.getIssuer(), "JWT issuer is required").trim();
    if (issuer.isBlank()) {
      throw new IllegalStateException("JWT issuer is required");
    }
    return JWT.require(algorithm()).withIssuer(issuer).build();
  }
}
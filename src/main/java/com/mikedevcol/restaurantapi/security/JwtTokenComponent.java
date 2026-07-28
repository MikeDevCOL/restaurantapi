package com.mikedevcol.restaurantapi.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.dto.response.TokenPairResponse;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;

@Component
public class JwtTokenComponent {
  private static final String ACCESS_TOKEN_TYPE = "access";
  private static final String REFRESH_TOKEN_TYPE = "refresh";
  private static final String TOKEN_TYPE_CLAIM = "token_type";

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final String issuer;
  private final Duration accessTokenTtl;
  private final Duration refreshTokenTtl;

  public JwtTokenComponent(
      @Value("${jwt.issuer}") String issuer,
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-expiration-seconds:900}") long accessTokenExpirationSeconds,
      @Value("${jwt.refresh-token-expiration-seconds:604800}") long refreshTokenExpirationSeconds) {

    if (secret == null || secret.length() < 32) {
      throw new IllegalArgumentException("JWT secret must contain at least 32 characters");
    }
    if (issuer == null || issuer.isBlank()) {
      throw new IllegalArgumentException("JWT issuer cannot be blank");
    }
    if (accessTokenExpirationSeconds <= 0 || refreshTokenExpirationSeconds <= 0) {
      throw new IllegalArgumentException("JWT expiration values must be greater than zero");
    }
    this.issuer = issuer;
    this.accessTokenTtl = Duration.ofSeconds(accessTokenExpirationSeconds);
    this.refreshTokenTtl = Duration.ofSeconds(refreshTokenExpirationSeconds);

    SecretKey secretKey = deriveSecretKey(secret, issuer);
    this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(secretKey));

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
    OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefaultWithIssuer(issuer);
    decoder.setJwtValidator(defaultValidator);
    this.jwtDecoder = decoder;
  }

  public TokenPairResponse createTokens(String subject) {
    if (subject == null || subject.isBlank()) {
      throw new IllegalArgumentException("Subject cannot be blank");
    }
    Instant issuedAt = Instant.now();
    String accessToken = createToken(subject, ACCESS_TOKEN_TYPE, issuedAt.plus(accessTokenTtl), issuedAt);
    String refreshToken = createToken(subject, REFRESH_TOKEN_TYPE, issuedAt.plus(refreshTokenTtl), issuedAt);
    return new TokenPairResponse(accessToken, refreshToken);
  }

  public Jwt readToken(String tokenValue) {
    return jwtDecoder.decode(tokenValue);
  }

  public boolean validateToken(String tokenValue) {
    try {
      readToken(tokenValue);
      return true;
    } catch (JwtException | IllegalArgumentException exception) {
      return false;
    }
  }

  public TokenPairResponse rotateTokens(String accessToken) {
    Jwt jwt = readToken(accessToken);
    if (!ACCESS_TOKEN_TYPE.equals(jwt.getClaimAsString(TOKEN_TYPE_CLAIM))) {
      throw new IllegalArgumentException("Token provided is not an access token");
    }
    return createTokens(jwt.getSubject());
  }

  private String createToken(String subject, String tokenType, Instant expiration, Instant issuedAt) {
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer(issuer)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiresAt(expiration)
        .claim(TOKEN_TYPE_CLAIM, tokenType)
        .build();

    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
    return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  private SecretKey deriveSecretKey(String secret, String issuer) {
    char[] password = secret.toCharArray();
    byte[] salt = issuer.getBytes(StandardCharsets.UTF_8);
    try {
      KeySpec keySpec = new PBEKeySpec(password, salt, 600_000, 256);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      byte[] keyBytes = factory.generateSecret(keySpec).getEncoded();
      return new SecretKeySpec(keyBytes, "HmacSHA256");
    } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
      throw new IllegalStateException("Unable to derive JWT secret key", exception);
    } finally {
      Arrays.fill(password, '\0');
    }
  }
}

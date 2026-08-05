package com.mikedevcol.restaurantapi.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.mikedevcol.restaurantapi.config.JwtProperties;
import com.mikedevcol.restaurantapi.core.dto.response.JwtTokenPairResponse;

class JwtTokenComponentTest {

  @Test
  void createReadValidateAndRotateShouldWork() {
    JwtTokenComponent component = new JwtTokenComponent(jwtProperties());

    JwtTokenPairResponse tokens = component.createTokens("owner-123");

    assertThat(tokens.accessToken()).isNotBlank();
    assertThat(tokens.refreshToken()).isNotBlank();
    assertThat(tokens.accessTokenExpiresAt()).isBefore(tokens.refreshTokenExpiresAt());
    assertThat(component.validateToken(tokens.accessToken())).isTrue();

    JwtTokenClaims accessClaims = component.readToken(tokens.accessToken());
    assertThat(accessClaims.subject()).isEqualTo("owner-123");
    assertThat(accessClaims.tokenType()).isEqualTo("access");

    JwtTokenPairResponse rotatedTokens = component.rotateTokens(tokens.accessToken());
    assertThat(rotatedTokens.accessToken()).isNotEqualTo(tokens.accessToken());
    assertThat(rotatedTokens.refreshToken()).isNotEqualTo(tokens.refreshToken());
  }

  @Test
  void rotateShouldFailWhenTokenIsNotAccess() {
    JwtTokenComponent component = new JwtTokenComponent(jwtProperties());
    JwtTokenPairResponse tokens = component.createTokens("owner-123");

    assertThatThrownBy(() -> component.rotateTokens(tokens.refreshToken()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("access token");
  }

  @Test
  void validateShouldReturnFalseForTamperedToken() {
    JwtTokenComponent component = new JwtTokenComponent(jwtProperties());
    JwtTokenPairResponse tokens = component.createTokens("owner-123");
    String tampered = tokens.accessToken() + "tampered";

    assertThat(component.validateToken(tampered)).isFalse();
  }

  private JwtProperties jwtProperties() {
    JwtProperties properties = new JwtProperties();
    properties.setIssuer("restaurantapi");
    properties.setSecret("0123456789abcdef0123456789abcdef");
    properties.setAccessTokenExpirationSeconds(300);
    properties.setRefreshTokenExpirationSeconds(600);
    return properties;
  }
}
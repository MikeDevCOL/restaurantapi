package com.mikedevcol.restaurantapi.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import com.mikedevcol.restaurantapi.dto.response.TokenPairResponse;

class JwtTokenComponentTest {
  private JwtTokenComponent jwtTokenComponent;

  @BeforeEach
  void setUp() {
    jwtTokenComponent = new JwtTokenComponent(
        "restaurantapi-test",
        "restaurantapi-test-secret-key-at-least-32",
        300,
        600);
  }

  @Test
  void createAndReadTokensShouldWork() {
    TokenPairResponse tokenPair = jwtTokenComponent.createTokens("user-1");

    assertThat(tokenPair.accessToken()).isNotBlank();
    assertThat(tokenPair.refreshToken()).isNotBlank();
    assertThat(jwtTokenComponent.validateToken(tokenPair.accessToken())).isTrue();
    assertThat(jwtTokenComponent.validateToken(tokenPair.refreshToken())).isTrue();

    Jwt accessJwt = jwtTokenComponent.readToken(tokenPair.accessToken());
    Jwt refreshJwt = jwtTokenComponent.readToken(tokenPair.refreshToken());

    assertThat(accessJwt.getSubject()).isEqualTo("user-1");
    assertThat(refreshJwt.getSubject()).isEqualTo("user-1");
    assertThat(accessJwt.getClaimAsString("token_type")).isEqualTo("access");
    assertThat(refreshJwt.getClaimAsString("token_type")).isEqualTo("refresh");
  }

  @Test
  void rotateTokensShouldGenerateNewPairFromAccessToken() {
    TokenPairResponse tokenPair = jwtTokenComponent.createTokens("user-2");

    TokenPairResponse rotatedTokenPair = jwtTokenComponent.rotateTokens(tokenPair.accessToken());

    assertThat(jwtTokenComponent.validateToken(rotatedTokenPair.accessToken())).isTrue();
    assertThat(jwtTokenComponent.validateToken(rotatedTokenPair.refreshToken())).isTrue();
    assertThat(jwtTokenComponent.readToken(rotatedTokenPair.accessToken()).getSubject()).isEqualTo("user-2");
  }

  @Test
  void rotateTokensShouldRejectRefreshToken() {
    TokenPairResponse tokenPair = jwtTokenComponent.createTokens("user-3");

    assertThatThrownBy(() -> jwtTokenComponent.rotateTokens(tokenPair.refreshToken()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("not an access token");
  }

  @Test
  void validateTokenShouldReturnFalseForInvalidToken() {
    assertThat(jwtTokenComponent.validateToken("invalid-token")).isFalse();
  }
}

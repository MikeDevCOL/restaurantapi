package com.mikedevcol.restaurantapi.dto.response;

public record TokenPairResponse(
    String accessToken,
    String refreshToken) {
}

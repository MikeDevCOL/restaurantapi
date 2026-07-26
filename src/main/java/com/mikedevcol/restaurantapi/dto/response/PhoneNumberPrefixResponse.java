package com.mikedevcol.restaurantapi.dto.response;

public record PhoneNumberPrefixResponse(
    Long id,
    String prefix,
    String country) {
}

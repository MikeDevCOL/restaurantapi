package com.mikedevcol.restaurantapi.core.dto.response;

public record PhoneNumberPrefixResponse(
        Long id,
        String prefix,
        String country) {
}

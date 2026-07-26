package com.mikedevcol.restaurantapi.dto.response;

public record OwnerContactInfoResponse(
    Long id,
    String email,
    String phoneNumber,
    PhoneNumberPrefixResponse phoneNumberPrefix) {
}

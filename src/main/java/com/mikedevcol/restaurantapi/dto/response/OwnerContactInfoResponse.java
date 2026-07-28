package com.mikedevcol.restaurantapi.dto.response;

public record OwnerContactInfoResponse(
        Long id,
        String phoneNumber,
        PhoneNumberPrefixResponse phoneNumberPrefix) {
}

package com.mikedevcol.restaurantapi.core.dto.response;

public record OwnerContactInfoResponse(
                Long id,
                String phoneNumber,
                PhoneNumberPrefixResponse phoneNumberPrefix) {
}

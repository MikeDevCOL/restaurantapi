package com.mikedevcol.restaurantapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneNumberPrefixCreateRequest(
    @NotBlank @Size(max = 10) String prefix,
    @NotBlank @Size(max = 50) String country) {
}

package com.mikedevcol.restaurantapi.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OwnerContactInfoCreateRequest(
                @NotBlank @Size(max = 20) String phoneNumber,
                @NotNull Long phoneNumberPrefixId) {
}

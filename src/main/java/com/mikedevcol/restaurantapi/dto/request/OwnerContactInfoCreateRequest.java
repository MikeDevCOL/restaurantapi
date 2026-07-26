package com.mikedevcol.restaurantapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OwnerContactInfoCreateRequest(
    @NotBlank @Email @Size(max = 100) String email,
    @NotBlank @Size(max = 20) String phoneNumber,
    @NotNull Long phoneNumberPrefixId) {
}

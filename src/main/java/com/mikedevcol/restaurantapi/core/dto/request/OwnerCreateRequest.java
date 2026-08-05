package com.mikedevcol.restaurantapi.core.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OwnerCreateRequest(
        @NotBlank @Size(max = 120) String firstName,
        @NotBlank @Size(max = 120) String lastName,
        @NotNull @Valid OwnerContactInfoCreateRequest contactInfo,
        List<@Valid OwnerRestaurantCreateRequest> restaurants) {
}

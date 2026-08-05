package com.mikedevcol.restaurantapi.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantCreateRequest(
        @NotBlank @Size(max = 10) String code,
        @NotNull Long ownerId) {
}

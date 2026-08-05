package com.mikedevcol.restaurantapi.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OwnerRestaurantCreateRequest(
        @NotBlank @Size(max = 10) String code) {
}

package com.mikedevcol.restaurantapi.core.dto.response;

import java.util.List;

public record OwnerResponse(
        Long id,
        String firstName,
        String lastName,
        OwnerContactInfoResponse contactInfo,
        List<RestaurantResponse> restaurants) {
}

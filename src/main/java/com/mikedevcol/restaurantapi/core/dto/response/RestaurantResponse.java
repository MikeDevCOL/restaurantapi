package com.mikedevcol.restaurantapi.core.dto.response;

public record RestaurantResponse(
        Long id,
        String code,
        Long ownerId) {
}

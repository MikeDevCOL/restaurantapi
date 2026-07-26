package com.mikedevcol.restaurantapi.dto.response;

public record RestaurantResponse(
    Long id,
    String code,
    Long ownerId) {
}

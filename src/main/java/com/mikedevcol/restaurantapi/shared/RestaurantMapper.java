package com.mikedevcol.restaurantapi.shared;

import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerRestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.RestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.Restaurant;

@Component
public class RestaurantMapper {

  public Restaurant toEntity(RestaurantCreateRequest request, Owner owner) {
    return Restaurant.builder()
        .code(request.code().trim())
        .owner(owner)
        .build();
  }

  public Restaurant toEntity(OwnerRestaurantCreateRequest request) {
    return Restaurant.builder()
        .code(request.code().trim())
        .build();
  }

  public RestaurantResponse toResponse(Restaurant entity) {
    Long ownerId = entity.getOwner() != null ? entity.getOwner().getId() : null;
    return new RestaurantResponse(entity.getId(), entity.getCode(), ownerId);
  }
}

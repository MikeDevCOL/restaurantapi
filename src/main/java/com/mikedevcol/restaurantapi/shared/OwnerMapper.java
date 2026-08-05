package com.mikedevcol.restaurantapi.shared;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerResponse;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.Restaurant;

@Component
public class OwnerMapper {

  private final OwnerContactInfoMapper contactInfoMapper;
  private final RestaurantMapper restaurantMapper;

  public OwnerMapper(OwnerContactInfoMapper contactInfoMapper, RestaurantMapper restaurantMapper) {
    this.contactInfoMapper = contactInfoMapper;
    this.restaurantMapper = restaurantMapper;
  }

  public Owner toEntity(OwnerCreateRequest request, OwnerContactInfo contactInfo, List<Restaurant> restaurants) {
    Owner owner = Owner.builder()
        .firstName(request.firstName().trim())
        .lastName(request.lastName().trim())
        .build();

    owner.assignContactInfo(contactInfo);
    if (restaurants != null) {
      restaurants.forEach(owner::addRestaurant);
    }
    return owner;
  }

  public OwnerResponse toResponse(Owner entity) {
    List<com.mikedevcol.restaurantapi.core.dto.response.RestaurantResponse> restaurants = entity.getRestaurants() == null
        ? List.of()
        : entity.getRestaurants().stream().map(restaurantMapper::toResponse).toList();

    com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse contactInfo = entity.getContactInfo() == null
        ? null
        : contactInfoMapper.toResponse(entity.getContactInfo());

    return new OwnerResponse(
        entity.getId(),
        entity.getFirstName(),
        entity.getLastName(),
        contactInfo,
        restaurants);
  }
}

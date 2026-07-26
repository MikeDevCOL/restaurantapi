package com.mikedevcol.restaurantapi.shared;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.dto.request.OwnerRestaurantCreateRequest;
import com.mikedevcol.restaurantapi.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.dto.request.RestaurantCreateRequest;
import com.mikedevcol.restaurantapi.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.dto.response.OwnerResponse;
import com.mikedevcol.restaurantapi.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.models.Restaurant;

@Component
public class RestaurantApiMapper {

  public PhoneNumberPrefix toEntity(PhoneNumberPrefixCreateRequest request) {
    return new PhoneNumberPrefix(null, request.prefix().trim(), request.country().trim());
  }

  public OwnerContactInfo toEntity(OwnerContactInfoCreateRequest request, PhoneNumberPrefix prefix) {
    return OwnerContactInfo.builder()
        .email(request.email().trim())
        .phoneNumber(request.phoneNumber().trim())
        .phoneNumberPrefix(prefix)
        .build();
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

  public PhoneNumberPrefixResponse toResponse(PhoneNumberPrefix entity) {
    return new PhoneNumberPrefixResponse(entity.getId(), entity.getPrefix(), entity.getCountry());
  }

  public OwnerContactInfoResponse toResponse(OwnerContactInfo entity) {
    return new OwnerContactInfoResponse(
        entity.getId(),
        entity.getEmail(),
        entity.getPhoneNumber(),
        toResponse(entity.getPhoneNumberPrefix()));
  }

  public RestaurantResponse toResponse(Restaurant entity) {
    Long ownerId = entity.getOwner() != null ? entity.getOwner().getId() : null;
    return new RestaurantResponse(entity.getId(), entity.getCode(), ownerId);
  }

  public OwnerResponse toResponse(Owner entity) {
    List<RestaurantResponse> restaurants = entity.getRestaurants() == null
        ? List.of()
        : entity.getRestaurants().stream().map(this::toResponse).toList();

    OwnerContactInfoResponse contactInfo = entity.getContactInfo() == null
        ? null
        : toResponse(entity.getContactInfo());

    return new OwnerResponse(
        entity.getId(),
        entity.getFirstName(),
        entity.getLastName(),
        contactInfo,
        restaurants);
  }

  public List<Restaurant> toEntities(List<OwnerRestaurantCreateRequest> restaurantRequests) {
    if (restaurantRequests == null) {
      return List.of();
    }
    return restaurantRequests.stream()
        .filter(Objects::nonNull)
        .map(this::toEntity)
        .toList();
  }
}

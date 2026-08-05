package com.mikedevcol.restaurantapi.shared;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.OwnerRestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.RestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerResponse;
import com.mikedevcol.restaurantapi.core.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.core.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.models.Restaurant;

@Component
public class RestaurantApiMapper {

  private final PhoneNumberPrefixMapper phoneNumberPrefixMapper;
  private final OwnerContactInfoMapper ownerContactInfoMapper;
  private final RestaurantMapper restaurantMapper;
  private final OwnerMapper ownerMapper;

  public RestaurantApiMapper(PhoneNumberPrefixMapper phoneNumberPrefixMapper,
      OwnerContactInfoMapper ownerContactInfoMapper,
      RestaurantMapper restaurantMapper,
      OwnerMapper ownerMapper) {
    this.phoneNumberPrefixMapper = phoneNumberPrefixMapper;
    this.ownerContactInfoMapper = ownerContactInfoMapper;
    this.restaurantMapper = restaurantMapper;
    this.ownerMapper = ownerMapper;
  }

  public PhoneNumberPrefix toEntity(PhoneNumberPrefixCreateRequest request) {
    return phoneNumberPrefixMapper.toEntity(request);
  }

  public OwnerContactInfo toEntity(OwnerContactInfoCreateRequest request, PhoneNumberPrefix prefix) {
    return ownerContactInfoMapper.toEntity(request, prefix);
  }

  public Owner toEntity(OwnerCreateRequest request, OwnerContactInfo contactInfo, List<Restaurant> restaurants) {
    return ownerMapper.toEntity(request, contactInfo, restaurants);
  }

  public Restaurant toEntity(RestaurantCreateRequest request, Owner owner) {
    return restaurantMapper.toEntity(request, owner);
  }

  public Restaurant toEntity(OwnerRestaurantCreateRequest request) {
    return restaurantMapper.toEntity(request);
  }

  public PhoneNumberPrefixResponse toResponse(PhoneNumberPrefix entity) {
    return phoneNumberPrefixMapper.toResponse(entity);
  }

  public OwnerContactInfoResponse toResponse(OwnerContactInfo entity) {
    return ownerContactInfoMapper.toResponse(entity);
  }

  public RestaurantResponse toResponse(Restaurant entity) {
    return restaurantMapper.toResponse(entity);
  }

  public OwnerResponse toResponse(Owner entity) {
    return ownerMapper.toResponse(entity);
  }

  public List<Restaurant> toEntities(List<OwnerRestaurantCreateRequest> restaurantRequests) {
    if (restaurantRequests == null) {
      return List.of();
    }
    return restaurantRequests.stream()
        .filter(Objects::nonNull)
        .map(restaurantMapper::toEntity)
        .toList();
  }
}

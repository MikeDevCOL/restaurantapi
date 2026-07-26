package com.mikedevcol.restaurantapi.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikedevcol.restaurantapi.dto.request.RestaurantCreateRequest;
import com.mikedevcol.restaurantapi.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.error.ConflictException;
import com.mikedevcol.restaurantapi.error.ResourceNotFoundException;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.Restaurant;
import com.mikedevcol.restaurantapi.repositories.OwnerRepository;
import com.mikedevcol.restaurantapi.repositories.RestaurantRepository;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final OwnerRepository ownerRepository;
  private final RestaurantApiMapper mapper;

  @Transactional
  public RestaurantResponse create(RestaurantCreateRequest request) {
    if (restaurantRepository.existsByCodeIgnoreCase(request.code().trim())) {
      throw new ConflictException("Restaurant code already exists");
    }

    Owner owner = ownerRepository.findById(request.ownerId())
        .orElseThrow(() -> new ResourceNotFoundException("Owner not found: " + request.ownerId()));

    Restaurant saved = restaurantRepository.save(mapper.toEntity(request, owner));
    return mapper.toResponse(saved);
  }
}

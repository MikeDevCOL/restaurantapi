package com.mikedevcol.restaurantapi.core.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.OwnerRestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerResponse;
import com.mikedevcol.restaurantapi.core.error.ConflictException;
import com.mikedevcol.restaurantapi.core.error.OperationFailedException;
import com.mikedevcol.restaurantapi.core.error.ResourceNotFoundException;
import com.mikedevcol.restaurantapi.core.models.Owner;
import com.mikedevcol.restaurantapi.core.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.core.models.Restaurant;
import com.mikedevcol.restaurantapi.core.repositories.OwnerContactInfoRepository;
import com.mikedevcol.restaurantapi.core.repositories.OwnerRepository;
import com.mikedevcol.restaurantapi.core.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.core.repositories.RestaurantRepository;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final OwnerContactInfoRepository ownerContactInfoRepository;
  private final PhoneNumberPrefixRepository phoneNumberPrefixRepository;
  private final RestaurantRepository restaurantRepository;
  private final RestaurantApiMapper mapper;

  /**
   * Creates owner aggregate in one transaction using JPA cascade for contact info
   * and restaurants.
   */
  @Transactional
  public OwnerResponse createWithDetails(OwnerCreateRequest request) {
    validateUniqueness(request);

    PhoneNumberPrefix prefix = phoneNumberPrefixRepository.findById(request.contactInfo().phoneNumberPrefixId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Phone number prefix not found: " + request.contactInfo().phoneNumberPrefixId()));

    OwnerContactInfo contactInfo = mapper.toEntity(request.contactInfo(), prefix);
    List<Restaurant> restaurants = mapper.toEntities(request.restaurants());

    Owner owner = mapper.toEntity(request, contactInfo, restaurants);

    try {
      Owner saved = ownerRepository.save(owner);
      return mapper.toResponse(saved);
    } catch (DataAccessException ex) {
      throw new OperationFailedException("Failed to create owner aggregate", ex);
    }
  }

  private void validateUniqueness(OwnerCreateRequest request) {

    if (ownerContactInfoRepository.existsByPhoneNumber(request.contactInfo().phoneNumber().trim())) {
      throw new ConflictException("Phone number already exists");
    }

    Set<String> requestCodes = new HashSet<>();
    if (request.restaurants() != null) {
      for (OwnerRestaurantCreateRequest item : request.restaurants()) {
        String normalizedCode = item.code().trim().toUpperCase();
        if (!requestCodes.add(normalizedCode)) {
          throw new ConflictException("Duplicate restaurant code in request: " + item.code());
        }
        if (restaurantRepository.existsByCodeIgnoreCase(normalizedCode)) {
          throw new ConflictException("Restaurant code already exists: " + item.code());
        }
      }
    }
  }
}

package com.mikedevcol.restaurantapi.core.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.core.error.ConflictException;
import com.mikedevcol.restaurantapi.core.error.ResourceNotFoundException;
import com.mikedevcol.restaurantapi.core.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.core.repositories.OwnerContactInfoRepository;
import com.mikedevcol.restaurantapi.core.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerContactInfoService {

  private final OwnerContactInfoRepository ownerContactInfoRepository;
  private final PhoneNumberPrefixRepository phoneNumberPrefixRepository;
  private final RestaurantApiMapper mapper;

  @Transactional
  public OwnerContactInfoResponse create(OwnerContactInfoCreateRequest request) {
    if (ownerContactInfoRepository.existsByPhoneNumber(request.phoneNumber().trim())) {
      throw new ConflictException("Phone number already exists");
    }

    PhoneNumberPrefix prefix = phoneNumberPrefixRepository.findById(request.phoneNumberPrefixId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Phone number prefix not found: " + request.phoneNumberPrefixId()));

    OwnerContactInfo saved = ownerContactInfoRepository.save(mapper.toEntity(request, prefix));
    return mapper.toResponse(saved);
  }
}

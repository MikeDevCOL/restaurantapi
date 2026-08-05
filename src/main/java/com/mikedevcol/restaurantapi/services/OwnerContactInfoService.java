package com.mikedevcol.restaurantapi.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.core.error.ConflictException;
import com.mikedevcol.restaurantapi.core.error.ResourceNotFoundException;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.repositories.OwnerContactInfoRepository;
import com.mikedevcol.restaurantapi.repositories.PhoneNumberPrefixRepository;
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

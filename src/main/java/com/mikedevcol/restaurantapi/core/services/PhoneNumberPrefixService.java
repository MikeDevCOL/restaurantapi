package com.mikedevcol.restaurantapi.core.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikedevcol.restaurantapi.core.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.core.error.ConflictException;
import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.core.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneNumberPrefixService {

  private final PhoneNumberPrefixRepository phoneNumberPrefixRepository;
  private final RestaurantApiMapper mapper;

  @Transactional
  public PhoneNumberPrefixResponse create(PhoneNumberPrefixCreateRequest request) {
    if (phoneNumberPrefixRepository.existsByPrefixAndCountryIgnoreCase(request.prefix().trim(),
        request.country().trim())) {
      throw new ConflictException("Phone prefix already exists for country");
    }

    PhoneNumberPrefix saved = phoneNumberPrefixRepository.save(mapper.toEntity(request));
    return mapper.toResponse(saved);
  }
}

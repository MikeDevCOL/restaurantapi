package com.mikedevcol.restaurantapi.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mikedevcol.restaurantapi.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.error.ConflictException;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.repositories.PhoneNumberPrefixRepository;
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

package com.mikedevcol.restaurantapi.shared;

import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.core.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;

@Component
public class PhoneNumberPrefixMapper {

  public PhoneNumberPrefix toEntity(PhoneNumberPrefixCreateRequest request) {
    return new PhoneNumberPrefix(null, request.prefix().trim(), request.country().trim());
  }

  public PhoneNumberPrefixResponse toResponse(PhoneNumberPrefix entity) {
    return new PhoneNumberPrefixResponse(entity.getId(), entity.getPrefix(), entity.getCountry());
  }
}

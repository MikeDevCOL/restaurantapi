package com.mikedevcol.restaurantapi.shared;

import org.springframework.stereotype.Component;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;

@Component
public class OwnerContactInfoMapper {

  private final PhoneNumberPrefixMapper prefixMapper;

  public OwnerContactInfoMapper(PhoneNumberPrefixMapper prefixMapper) {
    this.prefixMapper = prefixMapper;
  }

  public OwnerContactInfo toEntity(OwnerContactInfoCreateRequest request, PhoneNumberPrefix prefix) {
    return OwnerContactInfo.builder()
        .phoneNumber(request.phoneNumber().trim())
        .phoneNumberPrefix(prefix)
        .build();
  }

  public OwnerContactInfoResponse toResponse(OwnerContactInfo entity) {
    return new OwnerContactInfoResponse(
        entity.getId(),
        entity.getPhoneNumber(),
        prefixMapper.toResponse(entity.getPhoneNumberPrefix()));
  }
}

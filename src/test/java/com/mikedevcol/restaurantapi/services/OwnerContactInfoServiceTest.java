package com.mikedevcol.restaurantapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mikedevcol.restaurantapi.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.error.ResourceNotFoundException;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.repositories.OwnerContactInfoRepository;
import com.mikedevcol.restaurantapi.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

@ExtendWith(MockitoExtension.class)
class OwnerContactInfoServiceTest {

  @Mock
  private OwnerContactInfoRepository ownerContactInfoRepository;

  @Mock
  private PhoneNumberPrefixRepository phoneNumberPrefixRepository;

  @Mock
  private RestaurantApiMapper mapper;

  @InjectMocks
  private OwnerContactInfoService service;

  @Test
  void createShouldPersistWhenDependenciesExist() {
    OwnerContactInfoCreateRequest request = new OwnerContactInfoCreateRequest("a@b.com", "111", 1L);
    PhoneNumberPrefix prefix = new PhoneNumberPrefix(1L, "+57", "Colombia");
    OwnerContactInfo entity = OwnerContactInfo.builder().email("a@b.com").phoneNumber("111").phoneNumberPrefix(prefix)
        .build();
    OwnerContactInfo saved = OwnerContactInfo.builder().id(10L).email("a@b.com").phoneNumber("111")
        .phoneNumberPrefix(prefix).build();

    when(ownerContactInfoRepository.existsByEmailIgnoreCase("a@b.com")).thenReturn(false);
    when(ownerContactInfoRepository.existsByPhoneNumber("111")).thenReturn(false);
    when(phoneNumberPrefixRepository.findById(1L)).thenReturn(Optional.of(prefix));
    when(mapper.toEntity(request, prefix)).thenReturn(entity);
    when(ownerContactInfoRepository.save(entity)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(
        new OwnerContactInfoResponse(10L, "a@b.com", "111", new PhoneNumberPrefixResponse(1L, "+57", "Colombia")));

    OwnerContactInfoResponse result = service.create(request);

    assertThat(result.id()).isEqualTo(10L);
  }

  @Test
  void createShouldFailWhenPrefixDoesNotExist() {
    OwnerContactInfoCreateRequest request = new OwnerContactInfoCreateRequest("a@b.com", "111", 9L);
    when(ownerContactInfoRepository.existsByEmailIgnoreCase("a@b.com")).thenReturn(false);
    when(ownerContactInfoRepository.existsByPhoneNumber("111")).thenReturn(false);
    when(phoneNumberPrefixRepository.findById(9L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Phone number prefix not found");
  }
}

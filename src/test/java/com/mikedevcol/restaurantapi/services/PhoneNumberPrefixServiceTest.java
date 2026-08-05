package com.mikedevcol.restaurantapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mikedevcol.restaurantapi.core.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.core.error.ConflictException;
import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.core.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.core.services.PhoneNumberPrefixService;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

@ExtendWith(MockitoExtension.class)
class PhoneNumberPrefixServiceTest {

  @Mock
  private PhoneNumberPrefixRepository repository;

  @Mock
  private RestaurantApiMapper mapper;

  @InjectMocks
  private PhoneNumberPrefixService service;

  @Test
  void createShouldPersistWhenPrefixIsUnique() {
    PhoneNumberPrefixCreateRequest request = new PhoneNumberPrefixCreateRequest("+57", "Colombia");
    PhoneNumberPrefix entity = new PhoneNumberPrefix(null, "+57", "Colombia");
    PhoneNumberPrefix saved = new PhoneNumberPrefix(1L, "+57", "Colombia");
    PhoneNumberPrefixResponse response = new PhoneNumberPrefixResponse(1L, "+57", "Colombia");

    when(repository.existsByPrefixAndCountryIgnoreCase("+57", "Colombia")).thenReturn(false);
    when(mapper.toEntity(request)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(response);

    PhoneNumberPrefixResponse result = service.create(request);

    assertThat(result.id()).isEqualTo(1L);
    verify(repository).save(entity);
  }

  @Test
  void createShouldFailWhenPrefixAlreadyExists() {
    PhoneNumberPrefixCreateRequest request = new PhoneNumberPrefixCreateRequest("+57", "Colombia");
    when(repository.existsByPrefixAndCountryIgnoreCase("+57", "Colombia")).thenReturn(true);

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("Phone prefix already exists");
  }
}

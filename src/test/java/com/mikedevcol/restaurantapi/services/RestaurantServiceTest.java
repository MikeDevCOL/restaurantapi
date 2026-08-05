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

import com.mikedevcol.restaurantapi.core.dto.request.RestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.core.error.ResourceNotFoundException;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.Restaurant;
import com.mikedevcol.restaurantapi.repositories.OwnerRepository;
import com.mikedevcol.restaurantapi.repositories.RestaurantRepository;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

  @Mock
  private RestaurantRepository restaurantRepository;

  @Mock
  private OwnerRepository ownerRepository;

  @Mock
  private RestaurantApiMapper mapper;

  @InjectMocks
  private RestaurantService service;

  @Test
  void createShouldPersistWhenOwnerExists() {
    RestaurantCreateRequest request = new RestaurantCreateRequest("RST001", 1L);
    Owner owner = Owner.builder().id(1L).firstName("Ana").lastName("Perez").build();
    Restaurant entity = Restaurant.builder().code("RST001").owner(owner).build();
    Restaurant saved = Restaurant.builder().id(2L).code("RST001").owner(owner).build();
    RestaurantResponse response = new RestaurantResponse(2L, "RST001", 1L);

    when(restaurantRepository.existsByCodeIgnoreCase("RST001")).thenReturn(false);
    when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
    when(mapper.toEntity(request, owner)).thenReturn(entity);
    when(restaurantRepository.save(entity)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(response);

    RestaurantResponse result = service.create(request);

    assertThat(result.id()).isEqualTo(2L);
  }

  @Test
  void createShouldFailWhenOwnerDoesNotExist() {
    RestaurantCreateRequest request = new RestaurantCreateRequest("RST001", 99L);
    when(restaurantRepository.existsByCodeIgnoreCase("RST001")).thenReturn(false);
    when(ownerRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.create(request))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Owner not found");
  }
}

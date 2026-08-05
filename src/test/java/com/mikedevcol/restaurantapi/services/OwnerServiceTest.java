package com.mikedevcol.restaurantapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.request.OwnerRestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerResponse;
import com.mikedevcol.restaurantapi.core.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.core.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.core.error.ConflictException;
import com.mikedevcol.restaurantapi.core.models.Owner;
import com.mikedevcol.restaurantapi.core.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.core.models.Restaurant;
import com.mikedevcol.restaurantapi.core.repositories.OwnerContactInfoRepository;
import com.mikedevcol.restaurantapi.core.repositories.OwnerRepository;
import com.mikedevcol.restaurantapi.core.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.core.repositories.RestaurantRepository;
import com.mikedevcol.restaurantapi.core.services.OwnerService;
import com.mikedevcol.restaurantapi.shared.RestaurantApiMapper;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private OwnerContactInfoRepository ownerContactInfoRepository;

	@Mock
	private PhoneNumberPrefixRepository phoneNumberPrefixRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private RestaurantApiMapper mapper;

	@InjectMocks
	private OwnerService service;

	@Test
	void createWithDetailsShouldPersistAggregate() {
		OwnerCreateRequest request = new OwnerCreateRequest(
				"Ana",
				"Perez",
				new OwnerContactInfoCreateRequest("111", 1L),
				List.of(new OwnerRestaurantCreateRequest("RST001")));

		PhoneNumberPrefix prefix = new PhoneNumberPrefix(1L, "+57", "Colombia");
		OwnerContactInfo contactInfo = OwnerContactInfo.builder().phoneNumber("111")
				.phoneNumberPrefix(prefix).build();
		Restaurant restaurant = Restaurant.builder().code("RST001").build();
		Owner owner = Owner.builder().id(20L).firstName("Ana").lastName("Perez").restaurants(Set.of(restaurant))
				.contactInfo(contactInfo).build();

		OwnerResponse response = new OwnerResponse(
				20L,
				"Ana",
				"Perez",
				new OwnerContactInfoResponse(30L, "111",
						new PhoneNumberPrefixResponse(1L, "+57", "Colombia")),
				List.of(new RestaurantResponse(40L, "RST001", 20L)));

		when(ownerContactInfoRepository.existsByPhoneNumber("111")).thenReturn(false);
		when(restaurantRepository.existsByCodeIgnoreCase("RST001")).thenReturn(false);
		when(phoneNumberPrefixRepository.findById(1L)).thenReturn(Optional.of(prefix));
		when(mapper.toEntity(request.contactInfo(), prefix)).thenReturn(contactInfo);
		when(mapper.toEntities(request.restaurants())).thenReturn(List.of(restaurant));
		when(mapper.toEntity(request, contactInfo, List.of(restaurant))).thenReturn(owner);
		when(ownerRepository.save(owner)).thenReturn(owner);
		when(mapper.toResponse(owner)).thenReturn(response);

		OwnerResponse result = service.createWithDetails(request);

		assertThat(result.id()).isEqualTo(20L);
	}

	@Test
	void createWithDetailsShouldFailWhenRestaurantCodeRepeatedInRequest() {
		OwnerCreateRequest request = new OwnerCreateRequest(
				"Ana",
				"Perez",
				new OwnerContactInfoCreateRequest("111", 1L),
				List.of(new OwnerRestaurantCreateRequest("RST001"), new OwnerRestaurantCreateRequest("rst001")));

		when(ownerContactInfoRepository.existsByPhoneNumber("111")).thenReturn(false);

		assertThatThrownBy(() -> service.createWithDetails(request))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("Duplicate restaurant code in request");
	}
}

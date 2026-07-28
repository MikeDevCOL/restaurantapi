package com.mikedevcol.restaurantapi.shared;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mikedevcol.restaurantapi.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.dto.request.OwnerRestaurantCreateRequest;
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.models.Restaurant;

class RestaurantApiMapperTest {

  private final RestaurantApiMapper mapper = new RestaurantApiMapper();

  @Test
  void toEntityOwnerShouldAssignBidirectionalRestaurants() {
    OwnerCreateRequest request = new OwnerCreateRequest(
        "Ana",
        "Perez",
        new OwnerContactInfoCreateRequest("123", 1L),
        List.of(new OwnerRestaurantCreateRequest("RST001")));

    PhoneNumberPrefix prefix = new PhoneNumberPrefix(1L, "+57", "Colombia");
    OwnerContactInfo info = mapper.toEntity(request.contactInfo(), prefix);
    List<Restaurant> restaurants = mapper.toEntities(request.restaurants());

    Owner owner = mapper.toEntity(request, info, restaurants);

    assertThat(owner.getRestaurants()).hasSize(1);
    assertThat(owner.getRestaurants().iterator().next().getOwner()).isSameAs(owner);
    assertThat(owner.getContactInfo()).isSameAs(info);
  }
}

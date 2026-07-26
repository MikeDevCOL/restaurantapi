package com.mikedevcol.restaurantapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikedevcol.restaurantapi.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  boolean existsByCodeIgnoreCase(String code);

}

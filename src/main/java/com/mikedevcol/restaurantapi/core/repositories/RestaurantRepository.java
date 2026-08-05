package com.mikedevcol.restaurantapi.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikedevcol.restaurantapi.core.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  boolean existsByCodeIgnoreCase(String code);

}

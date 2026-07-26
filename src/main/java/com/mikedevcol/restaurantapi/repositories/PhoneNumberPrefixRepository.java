package com.mikedevcol.restaurantapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;

public interface PhoneNumberPrefixRepository extends JpaRepository<PhoneNumberPrefix, Long> {

  boolean existsByPrefixAndCountryIgnoreCase(String prefix, String country);
}

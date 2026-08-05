package com.mikedevcol.restaurantapi.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;

public interface PhoneNumberPrefixRepository extends JpaRepository<PhoneNumberPrefix, Long> {

  boolean existsByPrefixAndCountryIgnoreCase(String prefix, String country);
}

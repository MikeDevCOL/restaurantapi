package com.mikedevcol.restaurantapi.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikedevcol.restaurantapi.core.models.OwnerContactInfo;

public interface OwnerContactInfoRepository extends JpaRepository<OwnerContactInfo, Long> {

  boolean existsByPhoneNumber(String phoneNumber);
}

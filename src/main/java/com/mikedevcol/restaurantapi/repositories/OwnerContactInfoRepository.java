package com.mikedevcol.restaurantapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikedevcol.restaurantapi.models.OwnerContactInfo;

public interface OwnerContactInfoRepository extends JpaRepository<OwnerContactInfo, Long> {

  boolean existsByPhoneNumber(String phoneNumber);
}

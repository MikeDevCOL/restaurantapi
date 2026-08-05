package com.mikedevcol.restaurantapi.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikedevcol.restaurantapi.core.models.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

}
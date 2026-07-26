package com.mikedevcol.restaurantapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikedevcol.restaurantapi.models.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

}
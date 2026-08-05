package com.mikedevcol.restaurantapi.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikedevcol.restaurantapi.core.dto.request.RestaurantCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.RestaurantResponse;
import com.mikedevcol.restaurantapi.core.services.RestaurantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants")
@Validated
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;

  @PostMapping
  public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.create(request));
  }
}

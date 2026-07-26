package com.mikedevcol.restaurantapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikedevcol.restaurantapi.dto.request.OwnerCreateRequest;
import com.mikedevcol.restaurantapi.dto.response.OwnerResponse;
import com.mikedevcol.restaurantapi.services.OwnerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/owners")
@Validated
@RequiredArgsConstructor
public class OwnerController {

  private final OwnerService ownerService;

  @PostMapping
  public ResponseEntity<OwnerResponse> create(@Valid @RequestBody OwnerCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.createWithDetails(request));
  }
}

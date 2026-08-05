package com.mikedevcol.restaurantapi.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikedevcol.restaurantapi.core.dto.request.OwnerContactInfoCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.OwnerContactInfoResponse;
import com.mikedevcol.restaurantapi.core.services.OwnerContactInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/owner-contact-info")
@Validated
@RequiredArgsConstructor
public class OwnerContactInfoController {

  private final OwnerContactInfoService ownerContactInfoService;

  @PostMapping
  public ResponseEntity<OwnerContactInfoResponse> create(@Valid @RequestBody OwnerContactInfoCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ownerContactInfoService.create(request));
  }
}

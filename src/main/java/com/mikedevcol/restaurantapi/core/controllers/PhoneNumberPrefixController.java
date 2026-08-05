package com.mikedevcol.restaurantapi.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikedevcol.restaurantapi.core.dto.request.PhoneNumberPrefixCreateRequest;
import com.mikedevcol.restaurantapi.core.dto.response.PhoneNumberPrefixResponse;
import com.mikedevcol.restaurantapi.core.services.PhoneNumberPrefixService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/phone-prefixes")
@Validated
@RequiredArgsConstructor
public class PhoneNumberPrefixController {

  private final PhoneNumberPrefixService phoneNumberPrefixService;

  @PostMapping
  public ResponseEntity<PhoneNumberPrefixResponse> create(@Valid @RequestBody PhoneNumberPrefixCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(phoneNumberPrefixService.create(request));
  }
}
